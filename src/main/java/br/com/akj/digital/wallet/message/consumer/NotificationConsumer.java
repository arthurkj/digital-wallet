package br.com.akj.digital.wallet.message.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.akj.digital.wallet.message.dto.TransactionNotificationMessage;
import br.com.akj.digital.wallet.service.message.MessageService;
import br.com.akj.digital.wallet.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final MessageService messageService;
    private final NotificationService notificationService;

    @Value("${message.notification.queue.dlq.retries}")
    private int transactionNotificationMaxRetries;

    @Value("${message.notification.queue.plq.route}")
    private String transactionNotificationPlqRoute;

    @RabbitListener(queues = "${message.notification.queue.name}")
    public void receive(@Payload final TransactionNotificationMessage transactionNotificationMessage,
        final Message message) {
        log.info("Transaction notification message received: {}", transactionNotificationMessage);

        messageService.process(message, () -> notificationService.send(transactionNotificationMessage.senderId(),
                transactionNotificationMessage.receiverId(), transactionNotificationMessage.amount()),
            transactionNotificationMaxRetries, transactionNotificationPlqRoute);
    }
}
