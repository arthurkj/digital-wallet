package br.com.akj.digital.wallet.message.producer;

import br.com.akj.digital.wallet.fixture.Fixture;
import br.com.akj.digital.wallet.message.dto.TransactionNotificationMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationProducerTest {

    @InjectMocks
    private NotificationProducer producer;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(producer, "exchange", "exchange");
        ReflectionTestUtils.setField(producer, "routeKey", "routeKey");
    }

    @Test
    public void send_with_success() {
        final TransactionNotificationMessage transactionNotificationMessage = Fixture.make(TransactionNotificationMessage.class);

        producer.send(transactionNotificationMessage);

        verify(rabbitTemplate).convertAndSend("exchange", "routeKey", transactionNotificationMessage);
    }
}