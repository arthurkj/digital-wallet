package br.com.akj.digital.wallet.service.notification;

import static br.com.akj.digital.wallet.errors.Error.ERROR_ON_TRANSACTION_NOTIFICATION;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.com.akj.digital.wallet.exception.InternalErrorException;
import br.com.akj.digital.wallet.helper.MessageHelper;
import br.com.akj.digital.wallet.integration.notification.NotificationIntegration;
import br.com.akj.digital.wallet.integration.notification.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionNotificationService {

    private final NotificationIntegration notificationIntegration;
    private final MessageHelper messageHelper;

    public void send(final Long senderId, final Long receiverId, final BigDecimal amount) {
        try {
            sendTransactionNotification(senderId, receiverId, amount);
        } catch (final Exception e) {
            handleNotificationError(senderId, receiverId, e);
        }
    }

    private void sendTransactionNotification(Long senderId, Long receiverId, BigDecimal amount) {
        log.info("Sending notification to {} for a transaction of {} from {}", receiverId, amount, senderId);
        notificationIntegration.send();
    }

    private void handleNotificationError(Long senderId, Long receiverId, Exception e) {
        log.error("Error when try to notify a transaction between {} and {}: {}", senderId, receiverId,
                e.getMessage());
        throw new InternalErrorException(ERROR_ON_TRANSACTION_NOTIFICATION,
                messageHelper.get(ERROR_ON_TRANSACTION_NOTIFICATION));
    }
}
