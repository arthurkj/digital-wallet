package br.com.akj.digital.wallet.service.transaction;

import br.com.akj.digital.wallet.builder.notification.TransactionMessageBuilder;
import static br.com.akj.digital.wallet.errors.Error.UNAUTHORIZED_TRANSACTION;
import static br.com.akj.digital.wallet.integration.authorizer.dto.AuthorizerStatus.UNAUTHORIZED;

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
import br.com.akj.digital.wallet.integration.authorizer.dto.AuthorizerStatus;
import br.com.akj.digital.wallet.message.producer.NotificationProducer;
import br.com.akj.digital.wallet.repository.TransactionRepository;
import br.com.akj.digital.wallet.service.user.UserService;
import br.com.akj.digital.wallet.validator.transaction.TransactionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
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
    public TransactionResponse make(final TransactionRequest request) {
        log.info("Making transfer of {} from user {} to user {}", request.amount(), request.sender(),
                request.receiver());

        final UserEntity sender = userService.getById(request.sender());

        transactionValidator.validate(sender, request.receiver(), request.amount());

        final UserEntity receiver = userService.getById(request.receiver());

        final TransactionEntity transaction = TransactionBuilder.build(request, sender, receiver);

        return initialize(transaction);
    }

    private TransactionResponse initialize(final TransactionEntity transaction) {
        authorize(transaction);
        calculate(transaction);

        save(transaction, TransactionStatus.DONE);
        log.info("Transaction between {} and {} are completed", transaction.getSender().getId(), transaction.getReceiver().getId());

        notificationProducer.send(TransactionMessageBuilder.build(transaction.getSender().getId(), transaction.getReceiver().getId(), transaction.getAmount()));

        return new TransactionResponse(transaction.getStatus());
    }

    private void authorize(final TransactionEntity transaction) {
        final Boolean isAuthorized = authorizerService.authorize(transaction.getSender().getId(), transaction.getAmount());

        if (!isAuthorized) {
            saveUnauthorized(transaction);
        }
    }

    private void saveUnauthorized(final TransactionEntity transaction) {
        log.info("Transaction between {} and {} was unauthorized", transaction.getSender().getId(), transaction.getReceiver().getId());
        save(transaction, TransactionStatus.ERROR);

        throw new BusinessErrorException(UNAUTHORIZED_TRANSACTION, messageHelper.get(UNAUTHORIZED_TRANSACTION));
    }

    private void calculate(final TransactionEntity transaction) {
        final UserEntity sender = transaction.getSender();
        final BigDecimal amount = transaction.getAmount();

        transaction.getSender().setBalance(sender.getBalance().subtract(amount));

        final UserEntity receiver = transaction.getReceiver();
        receiver.setBalance(receiver.getBalance().add(amount));
    }

    private void save(final TransactionEntity transaction, final TransactionStatus status) {
        transaction.setStatus(status);
        transactionRepository.save(transaction);
    }
}
