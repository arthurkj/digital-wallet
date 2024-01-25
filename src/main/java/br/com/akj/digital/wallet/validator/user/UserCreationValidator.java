package br.com.akj.digital.wallet.validator.user;

import static br.com.akj.digital.wallet.errors.Error.NOT_UNIQUE_USER;

import br.com.akj.digital.wallet.domain.enumeration.UserType;
import br.com.akj.digital.wallet.dto.UserCreationRequest;
import br.com.akj.digital.wallet.exception.BusinessErrorException;
import br.com.akj.digital.wallet.helper.MessageHelper;
import br.com.akj.digital.wallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class UserCreationValidator {

    private final UserRepository userRepository;
    private final MessageHelper messageHelper;

    public abstract boolean acceptStrategy(final UserType type);

    abstract void validateCode(final String code);

    public void validate(final UserCreationRequest request) {
        validateCode(request.personRegistrationCode());

        if (userRepository.existsByPersonRegistrationCodeOrEmail(request.personRegistrationCode(), request.email())) {
            throw new BusinessErrorException(NOT_UNIQUE_USER, messageHelper.get(NOT_UNIQUE_USER));
        }
    }
}
