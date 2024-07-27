package br.com.akj.digital.wallet.builder.notification;

import br.com.akj.digital.wallet.domain.TransactionEntity;
import br.com.akj.digital.wallet.message.dto.TransactionNotificationMessage;

public class TransactionMessageBuilder {

    public static TransactionNotificationMessage build(final TransactionEntity transaction) {
        return new TransactionNotificationMessage(transaction.getSender().getId(), transaction.getReceiver().getId(), transaction.getAmount());
    }
}
