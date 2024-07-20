package br.com.akj.digital.wallet.message.consumer;

import br.com.akj.digital.wallet.fixture.Fixture;
import br.com.akj.digital.wallet.message.dto.TransactionNotificationMessage;
import br.com.akj.digital.wallet.service.message.MessageService;
import br.com.akj.digital.wallet.service.notification.NotificationService;
import br.com.akj.digital.wallet.utils.Procedure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @InjectMocks
    private NotificationConsumer consumer;

    @Mock
    private MessageService messageService;

    @Mock
    private NotificationService notificationService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(consumer, "transactionNotificationMaxRetries", 3);
        ReflectionTestUtils.setField(consumer, "transactionNotificationPlqRoute", "plqRoute");
    }

    @Test
    public void receive_with_success() {
        final TransactionNotificationMessage transactionNotificationMessage = Fixture.make(TransactionNotificationMessage.class);
        final Message message = mock(Message.class);

        consumer.receive(transactionNotificationMessage, message);

        ArgumentCaptor<Procedure> procedureCaptor = ArgumentCaptor.forClass(Procedure.class);
        verify(messageService).process(eq(message), procedureCaptor.capture(), eq(3), eq("plqRoute"));

        Procedure capturedProcedure = procedureCaptor.getValue();
        capturedProcedure.run();
        verify(notificationService).send(transactionNotificationMessage.senderId(), transactionNotificationMessage.receiverId(), transactionNotificationMessage.amount());
    }
}