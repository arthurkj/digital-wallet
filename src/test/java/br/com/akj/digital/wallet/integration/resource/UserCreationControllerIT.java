package br.com.akj.digital.wallet.integration.resource;

import static br.com.akj.digital.wallet.errors.Error.NOT_UNIQUE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.domain.enumeration.UserType;
import br.com.akj.digital.wallet.dto.user.UserCreationRequest;
import br.com.akj.digital.wallet.dto.user.UserResponse;
import br.com.akj.digital.wallet.errors.dto.ErrorDTO;
import br.com.akj.digital.wallet.integration.BaseIntegrationTest;
import br.com.akj.digital.wallet.repository.UserRepository;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;

public class UserCreationControllerIT extends BaseIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CPFValidator cpfValidator;

    @Autowired
    CNPJValidator cnpjValidator;

    @Test
    public void createCommonUser_ok() {
        final String name = RandomStringUtils.random(10);
        final String code = cpfValidator.generateRandomValid();
        final String email = RandomStringUtils.random(15);
        final String password = RandomStringUtils.randomNumeric(6);
        final UserType userType = UserType.COMMON;

        final UserCreationRequest request = new UserCreationRequest(name, code, email, password, userType);

        final UriComponentsBuilder uriBuilder = UriComponentsBuilder
            .fromUriString("/v1/user");

        final ResponseEntity<UserResponse> result = restTemplate.postForEntity(uriBuilder.toUriString(), request,
            UserResponse.class);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(name, result.getBody().name());

        final UserEntity userCreated = userRepository.findById(result.getBody().id()).get();

        assertNotNull(userCreated);
        assertEquals(name, userCreated.getName());
    }

    @Test
    public void createStoreOwnerUser_ok() {
        final String name = RandomStringUtils.random(10);
        final String code = cnpjValidator.generateRandomValid();
        final String email = RandomStringUtils.random(15);
        final String password = RandomStringUtils.randomNumeric(6);
        final UserType userType = UserType.STORE_OWNER;

        final UserCreationRequest request = new UserCreationRequest(name, code, email, password, userType);

        final UriComponentsBuilder uriBuilder = UriComponentsBuilder
            .fromUriString("/v1/user");

        final ResponseEntity<UserResponse> result = restTemplate.postForEntity(uriBuilder.toUriString(), request,
            UserResponse.class);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(name, result.getBody().name());

        final UserEntity userCreated = userRepository.findById(result.getBody().id()).get();

        assertNotNull(userCreated);
        assertEquals(name, userCreated.getName());
    }

    @Test
    public void createUser_error_not_unique() {
        final String name = RandomStringUtils.random(10);
        final String code = cnpjValidator.generateRandomValid();
        final String email = RandomStringUtils.random(15);
        final String password = RandomStringUtils.randomNumeric(6);
        final UserType userType = UserType.STORE_OWNER;

        final UserEntity oldUser = UserEntity.builder()
            .name(RandomStringUtils.random(10))
            .personRegistrationCode(code)
            .email(RandomStringUtils.random(15))
            .password(RandomStringUtils.randomNumeric(6))
            .type(UserType.COMMON)
            .build();

        userRepository.save(oldUser);


        final UserCreationRequest request = new UserCreationRequest(name, code, email, password, userType);

        final UriComponentsBuilder uriBuilder = UriComponentsBuilder
            .fromUriString("/v1/user");

        final ResponseEntity<ErrorDTO> result = restTemplate.postForEntity(uriBuilder.toUriString(), request,
            ErrorDTO.class);

        assertNotNull(result);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, result.getStatusCode());
        assertEquals(NOT_UNIQUE_USER.getCode(), result.getBody().code());
    }
}
