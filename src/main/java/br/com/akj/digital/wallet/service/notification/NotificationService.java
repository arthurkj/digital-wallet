package br.com.akj.digital.wallet.service.notification;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.com.akj.digital.wallet.integration.notification.NotificationIntegration;
import br.com.akj.digital.wallet.integration.notification.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationIntegration notificationIntegration;

    public void send(final Long senderId, final Long receiverId, final BigDecimal amount) {
        log.info("Sending notification to {} for a transaction of {} from {}", senderId, amount, receiverId);

        final NotificationResponse response = notificationIntegration.send();

        log.info("Notification sended: {}", response.message());
    }
}
