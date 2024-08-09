package br.com.akj.digital.wallet.service.transaction;

import static br.com.akj.digital.wallet.errors.Error.UNAUTHORIZED_TRANSACTION;

import java.math.BigDecimal;

import br.com.akj.digital.wallet.builder.notification.TransactionMessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final TransactionValidator transactionValidator;
    private final AuthorizerTransactionService authorizerTransactionService;
    private final NotificationProducer notificationProducer;
    private final MessageHelper messageHelper;

    @Transactional
    public TransactionResponse make(final TransactionRequest request) {
        log.info("Making transfer of {} from user {} to user {}", request.amount(), request.sender(),
            request.receiver());

        final UserEntity sender = userService.getById(request.sender());

        transactionValidator.validate(sender, request.receiver(), request.amount());

        return processValidTransaction(request, sender);
    }

    private TransactionResponse processValidTransaction(final TransactionRequest request, final UserEntity sender) {
        final UserEntity receiver = userService.getById(request.receiver());

        final boolean isAuthorized = authorizerTransactionService.authorize(sender, request.amount());

        if (!isAuthorized) {
            saveTransactionWithStatus(sender, receiver, request.amount(), TransactionStatus.ERROR);
            throw new BusinessErrorException(UNAUTHORIZED_TRANSACTION, messageHelper.get(UNAUTHORIZED_TRANSACTION));
        }

        return processAuthorizedTransaction(sender, request.amount(), receiver);
    }

    private TransactionResponse processAuthorizedTransaction(final UserEntity sender, final BigDecimal amount, final UserEntity receiver) {
        sender.decreaseBalance(amount);
        receiver.increaseBalance(amount);

        final TransactionEntity transaction = saveTransactionWithStatus(sender, receiver, amount, TransactionStatus.DONE);

        notificationProducer.send(TransactionMessageBuilder.build(transaction));

        return new TransactionResponse(transaction.getStatus());
    }

    private TransactionEntity saveTransactionWithStatus(final UserEntity sender, final UserEntity receiver, final BigDecimal amount, final TransactionStatus status) {
        final TransactionEntity transaction = TransactionBuilder.build(sender, receiver, amount, status);
        return transactionRepository.save(transaction);
    }
}
