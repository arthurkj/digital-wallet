package br.com.akj.digital.wallet.service.user;

import static br.com.akj.digital.wallet.errors.Error.CLIENT_NOT_FOUND;

import org.springframework.stereotype.Service;

import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.exception.BusinessErrorException;
import br.com.akj.digital.wallet.helper.MessageHelper;
import br.com.akj.digital.wallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MessageHelper messageHelper;

    public UserEntity getById(final Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new BusinessErrorException(CLIENT_NOT_FOUND, messageHelper.get(CLIENT_NOT_FOUND)));
    }
}
