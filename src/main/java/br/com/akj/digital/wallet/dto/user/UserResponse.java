package br.com.akj.digital.wallet.dto.user;

import br.com.akj.digital.wallet.domain.enumeration.UserType;

public record UserResponse(
    Long id,
    String name,
    String person_registration_code,
    String email,
    String password,
    UserType type) {

}
