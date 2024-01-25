package br.com.akj.digital.wallet.service.user;

import java.util.Collection;

import org.springframework.stereotype.Service;

import br.com.akj.digital.wallet.builder.user.UserEntityBuilder;
import br.com.akj.digital.wallet.builder.user.UserResponseBuilder;
import br.com.akj.digital.wallet.dto.UserCreationRequest;
import br.com.akj.digital.wallet.dto.UserResponse;
import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.repository.UserRepository;
import br.com.akj.digital.wallet.validator.user.UserCreationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCreationService {

    private final UserRepository userRepository;

    private final Collection<UserCreationValidator> creationValidators;

    public UserResponse create(final UserCreationRequest request) {
        log.info("Creating user {}", request.name());

        // TODO: mudar estratégia
        validate(request);

        final UserEntity newUser = userRepository.save(UserEntityBuilder.build(request));

        log.info("User {} created", newUser.getId());

        return UserResponseBuilder.build(newUser);
    }

    private void validate(final UserCreationRequest request) {
        creationValidators.stream().filter(v -> v.acceptStrategy(request.type()))
            .findFirst()
            .ifPresent(v -> v.validate(request));
    }
}
