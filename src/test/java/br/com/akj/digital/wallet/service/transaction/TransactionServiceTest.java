package br.com.akj.digital.wallet.service.transaction;

import static br.com.akj.digital.wallet.domain.enumeration.TransactionStatus.DONE;
import static br.com.akj.digital.wallet.domain.enumeration.TransactionStatus.ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import br.com.akj.digital.wallet.message.dto.TransactionNotificationMessage;
import br.com.akj.digital.wallet.message.producer.NotificationProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.akj.digital.wallet.domain.TransactionEntity;
import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.dto.transaction.TransactionRequest;
import br.com.akj.digital.wallet.dto.transaction.TransactionResponse;
import br.com.akj.digital.wallet.exception.BusinessErrorException;
import br.com.akj.digital.wallet.fixture.Fixture;
import br.com.akj.digital.wallet.helper.MessageHelper;
import br.com.akj.digital.wallet.integration.authorizer.dto.AuthorizerStatus;
import br.com.akj.digital.wallet.repository.TransactionRepository;
import br.com.akj.digital.wallet.service.notification.NotificationService;
import br.com.akj.digital.wallet.service.user.UserService;
import br.com.akj.digital.wallet.validator.transaction.TransactionValidator;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService service;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private TransactionValidator transactionValidator;

    @Mock
    private AuthorizerService authorizerService;

    @Mock
    private NotificationProducer notificationProducer;

    @Mock
    private MessageHelper messageHelper;

    @Test
    public void execute_with_success() {
        final TransactionRequest request = Fixture.make(TransactionRequest.class);

        final UserEntity sender = Fixture.make(UserEntity.class);
        final UserEntity receiver = Fixture.make(UserEntity.class);

        final AuthorizerStatus authorizationStatus = AuthorizerStatus.AUTHORIZED;

        when(userService.getById(request.sender())).thenReturn(sender);
        when(userService.getById(request.receiver())).thenReturn(receiver);

        when(authorizerService.authorize(sender.getId(), request.amount())).thenReturn(authorizationStatus);

        final TransactionResponse result = service.execute(request);

        assertNotNull(result);
        assertEquals(DONE, result.status());

        verify(userService).getById(request.sender());
        verify(transactionValidator).validate(sender, request.receiver(), request.amount());
        verify(userService).getById(request.receiver());
        verify(authorizerService).authorize(sender.getId(), request.amount());
        verify(transactionRepository).save(any(TransactionEntity.class));
        verify(notificationProducer).send(any(TransactionNotificationMessage.class));
    }

    @Test
    public void execute_unauthorized_transaction() {
        final TransactionRequest request = Fixture.make(TransactionRequest.class);

        final UserEntity sender = Fixture.make(UserEntity.class);
        final UserEntity receiver = Fixture.make(UserEntity.class);

        final AuthorizerStatus authorizationStatus = AuthorizerStatus.UNAUTHORIZED;

        when(userService.getById(request.sender())).thenReturn(sender);
        when(userService.getById(request.receiver())).thenReturn(receiver);

        when(authorizerService.authorize(sender.getId(), request.amount())).thenReturn(authorizationStatus);

        assertThrows(BusinessErrorException.class, () -> service.execute(request));

        verify(userService).getById(request.sender());
        verify(transactionValidator).validate(sender, request.receiver(), request.amount());
        verify(userService).getById(request.receiver());
        verify(authorizerService).authorize(sender.getId(), request.amount());
        verify(transactionRepository).save(any(TransactionEntity.class));
        verifyNoInteractions(notificationProducer);
    }
}