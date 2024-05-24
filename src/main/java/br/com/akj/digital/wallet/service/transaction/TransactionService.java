package br.com.akj.digital.wallet.service.transaction;

import static br.com.akj.digital.wallet.errors.Error.UNAUTHORIZED_TRANSACTION;
import static br.com.akj.digital.wallet.integration.authorizer.dto.AuthorizerStatus.UNAUTHORIZED;

import java.math.BigDecimal;

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
import br.com.akj.digital.wallet.integration.authorizer.dto.AuthorizerStatus;
import br.com.akj.digital.wallet.message.producer.NotificationProducer;
import br.com.akj.digital.wallet.repository.TransactionRepository;
import br.com.akj.digital.wallet.service.notification.NotificationService;
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

        final AuthorizerStatus authorizationStatus = authorizerService.authorize(sender.getId(), amount);

        if (UNAUTHORIZED.equals(authorizationStatus)) {
            final TransactionEntity transaction = TransactionBuilder.build(request, sender, receiver,
                TransactionStatus.ERROR);
            transactionRepository.save(transaction);

            throw new BusinessErrorException(UNAUTHORIZED_TRANSACTION, messageHelper.get(UNAUTHORIZED_TRANSACTION));
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        final TransactionEntity transaction = TransactionBuilder.build(request, sender, receiver,
            TransactionStatus.DONE);

        transactionRepository.save(transaction);

        notificationProducer.send(null);

        return new TransactionResponse(transaction.getStatus());
    }

}
