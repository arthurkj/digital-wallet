package br.com.akj.digital.wallet.service.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils.nextLong;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.exception.BusinessErrorException;
import br.com.akj.digital.wallet.fixture.Fixture;
import br.com.akj.digital.wallet.helper.MessageHelper;
import br.com.akj.digital.wallet.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository userRepository;
    @Mock
    private MessageHelper messageHelper;

    @Test
    public void getById_with_success() {
        final Long id = nextLong();

        final UserEntity entity = Fixture.make(UserEntity.class);

        when(userRepository.findById(id)).thenReturn(Optional.of(entity));

        final UserEntity result = service.getById(id);

        assertNotNull(result);
        assertEquals(entity, result);

        verify(userRepository).findById(id);
    }

    @Test
    public void getById_not_found() {
        final Long id = nextLong();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BusinessErrorException.class, () -> service.getById(id));

        verify(userRepository).findById(id);
    }
}