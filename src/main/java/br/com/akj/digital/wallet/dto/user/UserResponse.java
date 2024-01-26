package br.com.akj.digital.wallet.dto.user;

import br.com.akj.digital.wallet.domain.enumeration.UserType;

public record UserResponse(
    Long id,
    String name,
    String personRegistrationCode,
    String email,
    String password,
    UserType type) {

}
