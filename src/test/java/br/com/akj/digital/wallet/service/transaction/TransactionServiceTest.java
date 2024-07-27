package br.com.akj.digital.wallet.service.transaction;

import br.com.akj.digital.wallet.domain.TransactionEntity;
import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.dto.transaction.TransactionRequest;
import br.com.akj.digital.wallet.dto.transaction.TransactionResponse;
import br.com.akj.digital.wallet.exception.BusinessErrorException;
import br.com.akj.digital.wallet.fixture.Fixture;
import br.com.akj.digital.wallet.helper.MessageHelper;
import br.com.akj.digital.wallet.message.dto.TransactionNotificationMessage;
import br.com.akj.digital.wallet.message.producer.NotificationProducer;
import br.com.akj.digital.wallet.repository.TransactionRepository;
import br.com.akj.digital.wallet.service.user.UserService;
import br.com.akj.digital.wallet.validator.transaction.TransactionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static br.com.akj.digital.wallet.domain.enumeration.TransactionStatus.DONE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    private AuthorizerTransactionService authorizerTransactionService;

    @Mock
    private NotificationProducer notificationProducer;

    @Mock
    private MessageHelper messageHelper;

    @Test
    public void make_with_success() {
        final TransactionRequest request = Fixture.make(TransactionRequest.class);

        final UserEntity sender = Fixture.make(UserEntity.class);
        final UserEntity receiver = Fixture.make(UserEntity.class);

        final boolean isAuthorized = true;

        final TransactionEntity transactionEntity = TransactionEntity.builder()
                .sender(sender)
                .status(DONE)
                .receiver(receiver)
                .amount(request.amount())
                .build();

        when(userService.getById(request.sender())).thenReturn(sender);
        when(userService.getById(request.receiver())).thenReturn(receiver);

        when(authorizerTransactionService.authorize(sender, request.amount())).thenReturn(isAuthorized);

        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        final TransactionResponse result = service.make(request);

        assertNotNull(result);
        assertEquals(DONE, result.status());

        verify(userService).getById(request.sender());
        verify(transactionValidator).validate(sender, request.receiver(), request.amount());
        verify(userService).getById(request.receiver());
        verify(authorizerTransactionService).authorize(sender, request.amount());
        verify(transactionRepository).save(transactionEntity);
        verify(notificationProducer).send(any(TransactionNotificationMessage.class));
    }

    @Test
    public void make_unauthorized_transaction() {
        final TransactionRequest request = Fixture.make(TransactionRequest.class);

        final UserEntity sender = Fixture.make(UserEntity.class);
        final UserEntity receiver = Fixture.make(UserEntity.class);

        final boolean isAuthorized = false;

        when(userService.getById(request.sender())).thenReturn(sender);
        when(userService.getById(request.receiver())).thenReturn(receiver);

        when(authorizerTransactionService.authorize(sender, request.amount())).thenReturn(isAuthorized);

        assertThrows(BusinessErrorException.class, () -> service.make(request));

        verify(userService).getById(request.sender());
        verify(transactionValidator).validate(sender, request.receiver(), request.amount());
        verify(userService).getById(request.receiver());
        verify(authorizerTransactionService).authorize(sender, request.amount());
        verify(transactionRepository).save(any(TransactionEntity.class));
        verifyNoInteractions(notificationProducer);
    }
}