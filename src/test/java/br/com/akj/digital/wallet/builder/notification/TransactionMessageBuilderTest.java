package br.com.akj.digital.wallet.builder.notification;

import br.com.akj.digital.wallet.domain.TransactionEntity;
import br.com.akj.digital.wallet.fixture.Fixture;
import br.com.akj.digital.wallet.message.dto.TransactionNotificationMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransactionMessageBuilderTest {

    @Test
    public void build_with_success() {
        final TransactionEntity transaction = Fixture.make(TransactionEntity.class);

        final TransactionNotificationMessage result = TransactionMessageBuilder.build(transaction);

        assertNotNull(result);
        assertEquals(transaction.getSender().getId(), result.senderId());
        assertEquals(transaction.getReceiver().getId(), result.receiverId());
        assertEquals(transaction.getAmount(), result.amount());
    }
}