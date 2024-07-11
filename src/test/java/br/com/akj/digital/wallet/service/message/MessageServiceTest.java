package br.com.akj.digital.wallet.service.message;

import br.com.akj.digital.wallet.utils.Procedure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @InjectMocks
    private MessageService service;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(service, "exchange", "exchange");
    }

    @Test
    public void process_with_success() {
        final MessageProperties messageProperties = new MessageProperties();

        final Message message = new Message(new byte[]{}, messageProperties);
        final Procedure procedure = mock(Procedure.class);
        final int maxNumberOfRetries = 3;
        final String plqRoute = "plq";

        service.process(message, procedure, maxNumberOfRetries, plqRoute);

        verify(procedure).run();
    }

    @Test
    public void process_with_success_a_retry() {
        final Map<String, Object> xHeaders = new HashMap<>();
        final List<Object> headersList = new ArrayList<>();
        final Map<String, Object> headers = new HashMap<>();
        headers.put(MessageService.REASON_FIELD, MessageService.REJECTED_REASON);
        headers.put(MessageService.REPROCESS_NUMBER_OF_RETRIES_FIELD, 1);

        headersList.add(headers);
        xHeaders.put("x-death", headersList);

        final MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeaders(xHeaders);

        final Message message = new Message(new byte[]{}, messageProperties);
        final Procedure procedure = mock(Procedure.class);
        final int maxNumberOfRetries = 3;
        final String plqRoute = "plq";

        service.process(message, procedure, maxNumberOfRetries, plqRoute);

        verify(procedure).run();
    }

    @Test
    public void process_send_to_plq_when_reached_max_retries() {
        final int maxNumberOfRetries = 3;
        final Map<String, Object> xHeaders = new HashMap<>();
        final List<Object> headersList = new ArrayList<>();
        final Map<String, Object> headers = new HashMap<>();
        headers.put(MessageService.REASON_FIELD, MessageService.REJECTED_REASON);
        headers.put(MessageService.REPROCESS_NUMBER_OF_RETRIES_FIELD, maxNumberOfRetries);

        headersList.add(headers);
        xHeaders.put("x-death", headersList);

        final MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeaders(xHeaders);

        final Message message = new Message(new byte[]{}, messageProperties);
        final Procedure procedure = mock(Procedure.class);

        final String plqRoute = "plq";

        service.process(message, procedure, maxNumberOfRetries, plqRoute);

        verifyNoInteractions(procedure);
    }
}