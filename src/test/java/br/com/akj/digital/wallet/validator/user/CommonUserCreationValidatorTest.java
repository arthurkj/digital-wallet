package br.com.akj.digital.wallet.validator.user;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.akj.digital.wallet.domain.enumeration.UserType;
import br.com.akj.digital.wallet.dto.user.UserCreationRequest;
import br.com.akj.digital.wallet.exception.BusinessErrorException;
import br.com.akj.digital.wallet.fixture.Fixture;
import br.com.akj.digital.wallet.helper.MessageHelper;
import br.com.akj.digital.wallet.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CommonUserCreationValidatorTest {

    @InjectMocks
    private CommonUserCreationValidator validator;

    @Mock
    private CpfValidator cpfValidator;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MessageHelper messageHelper;

    @Test
    public void acceptStrategy_ok() {
        final UserType type = UserType.COMMON;

        final boolean result = validator.acceptStrategy(type);

        assertTrue(result);
    }

    @Test
    public void acceptStrategy_not_equal() {
        final UserType type = UserType.STORE_OWNER;

        final boolean result = validator.acceptStrategy(type);

        assertFalse(result);
    }

    @Test
    public void validateCode_with_success() {
        final String code = randomNumeric(11);

        validator.validateCode(code);

        verify(cpfValidator).validate(code);
    }

    @Test
    public void validate_with_success() {
        final UserCreationRequest request = Fixture.make(UserCreationRequest.class);

        when(userRepository.existsByPersonRegistrationCodeOrEmail(request.personRegistrationCode(), request.email())).thenReturn(false);

        validator.validate(request);

        verify(cpfValidator).validate(request.personRegistrationCode());
        verify(userRepository).existsByPersonRegistrationCodeOrEmail(request.personRegistrationCode(), request.email());
    }

    @Test
    public void validate_error_when_already_exists() {
        final UserCreationRequest request = Fixture.make(UserCreationRequest.class);

        when(userRepository.existsByPersonRegistrationCodeOrEmail(request.personRegistrationCode(), request.email())).thenReturn(true);

        assertThrows(BusinessErrorException.class, () -> validator.validate(request));

        verify(cpfValidator).validate(request.personRegistrationCode());
        verify(userRepository).existsByPersonRegistrationCodeOrEmail(request.personRegistrationCode(), request.email());
    }
}