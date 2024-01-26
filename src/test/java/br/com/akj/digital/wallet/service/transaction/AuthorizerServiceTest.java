package br.com.akj.digital.wallet.service.transaction;

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
import br.com.akj.digital.wallet.integration.authorizer.AuthorizerIntegration;
import br.com.akj.digital.wallet.integration.authorizer.dto.TransactionAuthorizationResponse;
import br.com.akj.digital.wallet.integration.notification.NotificationIntegration;
import br.com.akj.digital.wallet.integration.notification.dto.NotificationResponse;
import br.com.akj.digital.wallet.service.notification.NotificationService;

@ExtendWith(MockitoExtension.class)
class AuthorizerServiceTest {
    @InjectMocks
    private AuthorizerService service;

    @Mock
    private AuthorizerIntegration authorizerIntegration;

    @Test
    public void send_with_success() {
        final Long senderId = nextLong();
        final BigDecimal amount = BigDecimal.TEN;

        final TransactionAuthorizationResponse response = Fixture.make(TransactionAuthorizationResponse.class);

        when(authorizerIntegration.authorizeTransaction()).thenReturn(response);

        service.authorize(senderId, amount);

        verify(authorizerIntegration).authorizeTransaction();
    }
}