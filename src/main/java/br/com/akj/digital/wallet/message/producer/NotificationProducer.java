package br.com.akj.digital.wallet.message.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.akj.digital.wallet.message.dto.TransactionNotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {

    @Value("${message.exchange}")
    private String exchange;

    @Value("${message.notification.queue.route}")
    private String routeKey;

    private final RabbitTemplate rabbitTemplate;

    public void send(final TransactionNotificationMessage transactionNotificationMessage) {
        rabbitTemplate.convertAndSend(exchange, routeKey, transactionNotificationMessage);
    }
}
