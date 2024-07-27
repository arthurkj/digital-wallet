package br.com.akj.digital.wallet.service.transaction;

import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.integration.authorizer.AuthorizerIntegration;
import br.com.akj.digital.wallet.integration.authorizer.dto.AuthorizerStatus;
import br.com.akj.digital.wallet.integration.authorizer.dto.TransactionAuthorizationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorizerTransactionService {

    private final AuthorizerIntegration authorizerIntegration;

    public boolean authorize(final UserEntity sender, final BigDecimal amount) {
        log.info("Requesting authorization for transaction of {} from {}.", amount, sender);

        final TransactionAuthorizationResponse response = authorizerIntegration.authorizeTransaction();

        final AuthorizerStatus status = AuthorizerStatus.fromValue(response.message());
        log.info("Transaction of {} was {}", sender, status.getValue());

        return AuthorizerStatus.AUTHORIZED.equals(status);
    }
}
