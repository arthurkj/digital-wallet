package br.com.akj.digital.wallet.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MessageConfig {

    public static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    public static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
    public static final String X_MESSAGE_TTL = "x-message-ttl";

    @Value("${message.exchange}")
    private String exchange;

    @Value("${message.notification.queue.name}")
    private String notificationQueueName;

    @Value("${message.notification.queue.route}")
    private String notificationQueueRouteKey;

    @Value("${message.notification.queue.dlq.name}")
    private String notificationDlqName;

    @Value("${message.notification.queue.dlq.route}")
    private String notificationDlqRouteKey;

    @Value("${message.notification.queue.dlq.ttl}")
    private long notificationDlqTtl;

    @Value("${message.notification.queue.plq.name}")
    private String notificationPlqName;

    @Value("${message.notification.queue.plq.route}")
    private String notificationPlqRouteKey;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    Queue sendNotificationQueue() {
        return QueueBuilder.durable(notificationQueueName)
            .withArgument(X_DEAD_LETTER_EXCHANGE, exchange().getName())
            .withArgument(X_DEAD_LETTER_ROUTING_KEY, notificationQueueRouteKey)
            .build();
    }

    @Bean
    Queue sendNotificationErrorQueue() {
        return QueueBuilder.durable(notificationDlqName)
            .withArgument(X_DEAD_LETTER_EXCHANGE, exchange().getName())
            .withArgument(X_DEAD_LETTER_ROUTING_KEY, notificationQueueRouteKey)
            .withArgument(X_MESSAGE_TTL, notificationDlqTtl)
            .build();
    }

    @Bean
    Queue sendNotificationParkinglotQueue() {
        return QueueBuilder.durable(notificationPlqName).build();
    }

    @Bean
    Binding sendNotificationQueueToExchangeBinder() {
        return BindingBuilder.bind(sendNotificationQueue())
            .to(exchange())
            .with(notificationQueueRouteKey);
    }

    @Bean
    Binding sendNotificationErrorQueueToExchangeBinder() {
        return BindingBuilder.bind(sendNotificationErrorQueue())
            .to(exchange())
            .with(notificationDlqRouteKey);
    }

    @Bean
    Binding sendNotificationParkinglotQueueToExchangeBinder() {
        return BindingBuilder.bind(sendNotificationParkinglotQueue())
            .to(exchange())
            .with(notificationPlqRouteKey);
    }
}
