package br.com.akj.digital.wallet.builder.notification;

import br.com.akj.digital.wallet.message.dto.TransactionNotificationMessage;

import java.math.BigDecimal;

public class TransactionMessageBuilder {

    public static TransactionNotificationMessage build(final Long senderId, final Long receiverId, final BigDecimal amount) {
        return new TransactionNotificationMessage(senderId, receiverId, amount);
    }
}
