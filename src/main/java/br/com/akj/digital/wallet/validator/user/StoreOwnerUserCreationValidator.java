package br.com.akj.digital.wallet.validator.user;

import org.springframework.stereotype.Component;

import br.com.akj.digital.wallet.domain.enumeration.UserType;
import br.com.akj.digital.wallet.helper.MessageHelper;
import br.com.akj.digital.wallet.repository.UserRepository;

@Component
public class StoreOwnerUserCreationValidator extends UserCreationValidator {

    private final CnpjValidator cnpjValidator;

    public StoreOwnerUserCreationValidator(final UserRepository userRepository, final MessageHelper messageHelper,
        final CnpjValidator cnpjValidator) {
        super(userRepository, messageHelper);
        this.cnpjValidator = cnpjValidator;
    }

    @Override
    public boolean acceptStrategy(final UserType type) {
        return UserType.STORE_OWNER.equals(type);
    }

    @Override
    public void validateCode(final String code) {
        cnpjValidator.validate(code);
    }
}
