package br.com.akj.digital.wallet.validator.user;

import org.springframework.stereotype.Component;

import br.com.akj.digital.wallet.domain.enumeration.UserType;
import br.com.akj.digital.wallet.helper.MessageHelper;
import br.com.akj.digital.wallet.repository.UserRepository;

@Component
public class CommonUserCreationValidator extends UserCreationValidator {

    private final CpfValidator cpfValidator;

    public CommonUserCreationValidator(final UserRepository userRepository, final MessageHelper messageHelper,
        final CpfValidator cpfValidator) {
        super(userRepository, messageHelper);
        this.cpfValidator = cpfValidator;
    }

    @Override
    public boolean acceptStrategy(final UserType type) {
        return UserType.COMMON.equals(type);
    }

    @Override
    public void validateCode(final String code) {
        cpfValidator.validate(code);
    }
}
