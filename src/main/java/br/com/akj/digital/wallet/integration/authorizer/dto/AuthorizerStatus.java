package br.com.akj.digital.wallet.integration.authorizer.dto;

import java.util.Arrays;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AuthorizerStatus {

    AUTHORIZED("Autorizado"),
    UNAUTHORIZED("Não autorizado");

    private final String value;

    public static AuthorizerStatus fromValue(final String value) {
        return Arrays.stream(AuthorizerStatus.values()).filter(enumValue -> enumValue.value.equals(value)).findFirst()
            .orElse(null);
    }
}
