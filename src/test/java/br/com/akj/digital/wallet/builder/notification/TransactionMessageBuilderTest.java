package br.com.akj.digital.wallet.builder.notification;

import br.com.akj.digital.wallet.message.dto.TransactionNotificationMessage;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils.nextLong;

public class TransactionMessageBuilderTest {

    @Test
    public void build_with_success() {
        final Long senderId = nextLong();
        final Long receiverId = nextLong();
        final BigDecimal amount = BigDecimal.TEN;

        final TransactionNotificationMessage result = TransactionMessageBuilder.build(senderId, receiverId, amount);

        assertNotNull(result);
        assertEquals(senderId, result.senderId());
        assertEquals(receiverId, result.receiverId());
        assertEquals(amount, result.amount());
    }
}