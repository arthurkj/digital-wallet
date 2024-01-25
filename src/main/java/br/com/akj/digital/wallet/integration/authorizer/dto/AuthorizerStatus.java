package br.com.akj.digital.wallet.integration.authorizer.dto;

import java.util.Arrays;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthorizerStatus {

    AUTHORIZED("Autorizado"),
    UNAUTHORIZED("Não autorizado");

    private final String value;

    public static AuthorizerStatus fromValue(final String value) {
        return Arrays.stream(AuthorizerStatus.values()).filter(enumValue -> enumValue.value.equals(value)).findFirst()
            .orElse(null);
    }
}
