package br.com.akj.digital.wallet.builder.transaction;

import br.com.akj.digital.wallet.domain.TransactionEntity;
import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.domain.enumeration.TransactionStatus;
import br.com.akj.digital.wallet.dto.transaction.TransactionRequest;

import java.math.BigDecimal;

public class TransactionBuilder {

    public static TransactionEntity build(final UserEntity sender,
                                          final UserEntity receiver, final BigDecimal amount, final TransactionStatus status) {
        return TransactionEntity.builder()
            .status(status)
            .sender(sender)
            .receiver(receiver)
            .amount(amount)
            .build();
    }
}
