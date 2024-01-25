package br.com.akj.digital.wallet.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.akj.digital.wallet.domain.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    boolean existsByPersonRegistrationCodeOrEmail(final String code, final String email);
}
