package br.com.akj.digital.wallet.integration.authorizer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.akj.digital.wallet.integration.authorizer.dto.TransactionAuthorizationResponse;

@FeignClient(name = "AuthorizerIntegration", url = "${integration.api.authorizer.host}${integration.api.authorizer.v3.basePath}")
public interface AuthorizerIntegration {

    @GetMapping(value = "${integration.api.authorizer.v3.authorize}", consumes = MediaType.APPLICATION_JSON_VALUE)
    TransactionAuthorizationResponse authorizeTransaction();
}
