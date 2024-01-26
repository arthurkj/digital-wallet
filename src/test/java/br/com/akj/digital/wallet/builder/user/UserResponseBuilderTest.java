package br.com.akj.digital.wallet.builder.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.dto.user.UserResponse;
import br.com.akj.digital.wallet.fixture.Fixture;

public class UserResponseBuilderTest {

    @Test
    public void build_with_success() {
        final UserEntity entity = Fixture.make(UserEntity.class);

        final UserResponse result = UserResponseBuilder.build(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getName(), result.name());
        assertEquals(entity.getPersonRegistrationCode(), result.personRegistrationCode());
        assertEquals(entity.getEmail(), result.email());
        assertEquals(entity.getPassword(), result.password());
        assertEquals(entity.getType(), result.type());
    }
}