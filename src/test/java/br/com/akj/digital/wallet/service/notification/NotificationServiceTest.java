package br.com.akj.digital.wallet.service.notification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils.nextLong;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.akj.digital.wallet.fixture.Fixture;
import br.com.akj.digital.wallet.integration.notification.NotificationIntegration;
import br.com.akj.digital.wallet.integration.notification.dto.NotificationResponse;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService service;

    @Mock
    private NotificationIntegration notificationIntegration;

    @Test
    public void send_with_success() {
        final Long senderId = nextLong();
        final Long receiverId = nextLong();
        final BigDecimal amount = BigDecimal.TEN;

        final NotificationResponse response = Fixture.make(NotificationResponse.class);

        when(notificationIntegration.send()).thenReturn(response);

        service.send(senderId, receiverId, amount);

        verify(notificationIntegration).send();
    }
}