package br.com.akj.digital.wallet.service.transaction;

import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.fixture.Fixture;
import br.com.akj.digital.wallet.integration.authorizer.AuthorizerIntegration;
import br.com.akj.digital.wallet.integration.authorizer.dto.AuthorizerStatus;
import br.com.akj.digital.wallet.integration.authorizer.dto.TransactionAuthorizationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizerTransactionServiceTest {
    @InjectMocks
    private AuthorizerTransactionService service;

    @Mock
    private AuthorizerIntegration authorizerIntegration;

    @Test
    public void authorize_with_success() {
        final UserEntity sender = Fixture.make(UserEntity.class);
        final BigDecimal amount = BigDecimal.TEN;

        final TransactionAuthorizationResponse response = new TransactionAuthorizationResponse(AuthorizerStatus.AUTHORIZED.getValue());

        when(authorizerIntegration.authorizeTransaction()).thenReturn(response);

        final boolean result = service.authorize(sender, amount);

        assertTrue(result);

        verify(authorizerIntegration).authorizeTransaction();
    }

    @Test
    public void authorize_return_false_when_unauthorized() {
        final UserEntity sender = Fixture.make(UserEntity.class);
        final BigDecimal amount = BigDecimal.TEN;

        final TransactionAuthorizationResponse response = new TransactionAuthorizationResponse(AuthorizerStatus.UNAUTHORIZED.getValue());

        when(authorizerIntegration.authorizeTransaction()).thenReturn(response);

        final boolean result = service.authorize(sender, amount);

        assertFalse(result);

        verify(authorizerIntegration).authorizeTransaction();
    }
}