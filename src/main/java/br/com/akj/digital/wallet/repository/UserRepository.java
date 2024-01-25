package br.com.akj.digital.wallet.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.akj.digital.wallet.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByName(String name);
}
