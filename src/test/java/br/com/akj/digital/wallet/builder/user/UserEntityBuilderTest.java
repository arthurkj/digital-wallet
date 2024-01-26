package br.com.akj.digital.wallet.builder.user;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.dto.user.UserCreationRequest;
import br.com.akj.digital.wallet.fixture.Fixture;

class UserEntityBuilderTest {

    @Test
    public void build_with_success() {
        final UserCreationRequest request = Fixture.make(UserCreationRequest.class);

        final UserEntity result = UserEntityBuilder.build(request);

        assertNotNull(result);
        assertEquals(request.name(), result.getName());
        assertEquals(request.personRegistrationCode(), result.getPersonRegistrationCode());
        assertEquals(request.email(), result.getEmail());
        assertEquals(request.password(), result.getPassword());
        assertEquals(request.type(), result.getType());
        assertEquals(BigDecimal.ZERO, result.getBalance());
    }
}