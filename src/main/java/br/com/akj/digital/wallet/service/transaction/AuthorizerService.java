package br.com.akj.digital.wallet.service.transaction;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.com.akj.digital.wallet.integration.authorizer.AuthorizerIntegration;
import br.com.akj.digital.wallet.integration.authorizer.dto.AuthorizerStatus;
import br.com.akj.digital.wallet.integration.authorizer.dto.TransactionAuthorizationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorizerService {

    private final AuthorizerIntegration authorizerIntegration;

    public Boolean authorize(final Long senderId, final BigDecimal amount) {
        log.info("Requesting authorization for transaction of {} from {}.", amount, senderId);

        final TransactionAuthorizationResponse response = authorizerIntegration.authorizeTransaction();

        log.info("Result of requesting from {}: {}", senderId, response.message());

        return AuthorizerStatus.AUTHORIZED.equals(AuthorizerStatus.fromValue(response.message()));
    }
}
