package br.com.akj.digital.wallet.builder.user;

import java.math.BigDecimal;

import br.com.akj.digital.wallet.dto.user.UserCreationRequest;
import br.com.akj.digital.wallet.domain.UserEntity;

public class UserEntityBuilder {

    public static UserEntity build(final UserCreationRequest request) {
        return UserEntity.builder()
            .name(request.name())
            .personRegistrationCode(request.personRegistrationCode())
            .email(request.email())
            .password(request.password())
            .type(request.type())
            .balance(BigDecimal.ZERO)
            .build();
    }
}
