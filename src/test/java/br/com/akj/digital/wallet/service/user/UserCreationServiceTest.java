package br.com.akj.digital.wallet.service.user;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.dto.user.UserCreationRequest;
import br.com.akj.digital.wallet.dto.user.UserResponse;
import br.com.akj.digital.wallet.fixture.Fixture;
import br.com.akj.digital.wallet.repository.UserRepository;
import br.com.akj.digital.wallet.validator.user.CommonUserCreationValidator;

@ExtendWith(MockitoExtension.class)
class UserCreationServiceTest {

    private UserCreationService service;
    private UserRepository userRepository;

    private CommonUserCreationValidator validator;

    @BeforeEach
    void setUp() {
        validator = mock(CommonUserCreationValidator.class);
        userRepository = mock(UserRepository.class);

        service = new UserCreationService(userRepository, singleton(validator));
    }

    @Test
    public void create_with_success() {
        final UserCreationRequest request = Fixture.make(UserCreationRequest.class);

        final UserEntity newUser = Fixture.make(UserEntity.class);

        when(validator.acceptStrategy(request.type())).thenReturn(true);
        when(userRepository.save(any(UserEntity.class))).thenReturn(newUser);

        final UserResponse result = service.create(request);

        assertNotNull(result);
        assertEquals(newUser.getId(), result.id());

        verify(validator).acceptStrategy(request.type());
        verify(validator).validate(request);

        verify(userRepository).save(any(UserEntity.class));
    }
}