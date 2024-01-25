package br.com.akj.digital.wallet.builder.user;

import br.com.akj.digital.wallet.dto.UserResponse;
import br.com.akj.digital.wallet.domain.UserEntity;

public class UserResponseBuilder {

    public static UserResponse build(final UserEntity entity) {
        return new UserResponse(entity.getId(), entity.getName(), entity.getPersonRegistrationCode(),
            entity.getEmail(), entity.getPassword(), entity.getType());
    }
}
