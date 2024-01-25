package br.com.akj.digital.wallet.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.akj.digital.wallet.domain.TransactionEntity;
import br.com.akj.digital.wallet.domain.UserEntity;

public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {


}
