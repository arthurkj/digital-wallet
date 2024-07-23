package br.com.akj.digital.wallet.service.transaction;

import br.com.akj.digital.wallet.builder.notification.TransactionMessageBuilder;
import br.com.akj.digital.wallet.builder.transaction.TransactionBuilder;
import br.com.akj.digital.wallet.domain.TransactionEntity;
import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.domain.enumeration.TransactionStatus;
import br.com.akj.digital.wallet.dto.transaction.TransactionRequest;
import br.com.akj.digital.wallet.dto.transaction.TransactionResponse;
import br.com.akj.digital.wallet.exception.BusinessErrorException;
import br.com.akj.digital.wallet.helper.MessageHelper;
import br.com.akj.digital.wallet.message.producer.NotificationProducer;
import br.com.akj.digital.wallet.repository.TransactionRepository;
import br.com.akj.digital.wallet.service.user.UserService;
import br.com.akj.digital.wallet.validator.transaction.TransactionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static br.com.akj.digital.wallet.errors.Error.UNAUTHORIZED_TRANSACTION;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final TransactionValidator transactionValidator;
    private final AuthorizerService authorizerService;
    private final NotificationProducer notificationProducer;
    private final MessageHelper messageHelper;

    @Transactional
    public TransactionResponse execute(final TransactionRequest request) {
        log.info("Making transfer of {} from user {} to user {}", request.amount(), request.sender(),
                request.receiver());

        final UserEntity sender = userService.getById(request.sender());
        final BigDecimal amount = request.amount();

        transactionValidator.validate(sender, request.receiver(), amount);

        final UserEntity receiver = userService.getById(request.receiver());

        return make(request, sender, receiver, amount);
    }

    private TransactionResponse make(TransactionRequest request, UserEntity sender, UserEntity receiver, BigDecimal amount) {
        final TransactionEntity transaction = TransactionBuilder.build(request, sender, receiver);

        authorize(transaction);

        complete(sender, amount, receiver, transaction);

        notificationProducer.send(TransactionMessageBuilder.build(sender.getId(), receiver.getId(), amount));

        return new TransactionResponse(transaction.getStatus());
    }

    private void authorize(TransactionEntity transaction) {
        final Boolean isAuthorized = authorizerService.authorize(transaction.getSender().getId(), transaction.getAmount());

        if (!isAuthorized) {
            saveUnauthorized(transaction);
        }
    }

    private void saveUnauthorized(TransactionEntity transaction) {
        log.info("Transaction between {} and {} was unauthorized", transaction.getSender().getId(), transaction.getReceiver().getId());
        save(transaction, TransactionStatus.ERROR);

        throw new BusinessErrorException(UNAUTHORIZED_TRANSACTION, messageHelper.get(UNAUTHORIZED_TRANSACTION));
    }

    private void complete(UserEntity sender, BigDecimal amount, UserEntity receiver, TransactionEntity transaction) {
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        save(transaction, TransactionStatus.DONE);
    }

    private void save(TransactionEntity transaction, TransactionStatus error) {
        transaction.setStatus(error);
        transactionRepository.save(transaction);
    }

}
