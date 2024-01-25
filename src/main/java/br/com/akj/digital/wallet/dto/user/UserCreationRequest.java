package br.com.akj.digital.wallet.dto.user;

import br.com.akj.digital.wallet.domain.enumeration.UserType;
import jakarta.validation.constraints.NotBlank;

public record UserCreationRequest(@NotBlank
                                  String name,
                                  @NotBlank
                                  String personRegistrationCode,
                                  @NotBlank
                                  String email,
                                  @NotBlank
                                  String password,
                                  @NotBlank
                                  UserType type) {

}
