package br.com.akj.digital.wallet.builder.transaction;

import br.com.akj.digital.wallet.domain.TransactionEntity;
import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.domain.enumeration.TransactionStatus;
import br.com.akj.digital.wallet.dto.transaction.TransactionRequest;

public class TransactionBuilder {

    public static TransactionEntity build(final TransactionRequest request, final UserEntity sender,
        final UserEntity receiver, final TransactionStatus status) {
        return TransactionEntity.builder()
            .status(status)
            .sender(sender)
            .receiver(receiver)
            .amount(request.amount())
            .build();
    }
}
