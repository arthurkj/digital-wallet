package br.com.akj.digital.wallet.service.message;

import static java.util.Optional.ofNullable;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.akj.digital.wallet.utils.Procedure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageService {

    public static final String REASON_FIELD = "reason";
    public static final String REPROCESS_NUMBER_OF_RETRIES_FIELD = "count";
    public static final String REJECTED_REASON = "rejected";

    private final RabbitTemplate rabbitTemplate;

    @Value("${message.exchange}")
    private String exchange;

    public void process(final Message message, final Procedure procedure, final int maxNumberOfRetries,
        final String plqRoute) {

        final int numberOfRetries = getNumberOfRetries(message);

        if (numberOfRetries >= maxNumberOfRetries) {
            sendToParkingLot(message, plqRoute);
        } else {
            procedure.run();
        }
    }

    private Integer getNumberOfRetries(final Message message) {
        return ofNullable(message.getMessageProperties().getXDeathHeader()).flatMap(l -> l.stream()
                .filter(map -> map.get(REASON_FIELD).equals(REJECTED_REASON))
                .findFirst())
            .map(map -> map.get(REPROCESS_NUMBER_OF_RETRIES_FIELD))
            .map(String::valueOf)
            .map(Integer::valueOf)
            .orElse(0);
    }

    public void sendToParkingLot(final Message failedMessage, final String route) {
        log.warn("Max number of retries has been reached. Sending message to parking lot. route: {}", route);

        rabbitTemplate.convertAndSend(exchange, route, failedMessage);
    }

}
