package br.com.akj.digital.wallet.builder.transaction;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import br.com.akj.digital.wallet.domain.TransactionEntity;
import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.domain.enumeration.TransactionStatus;
import br.com.akj.digital.wallet.dto.transaction.TransactionRequest;
import br.com.akj.digital.wallet.fixture.Fixture;

public class TransactionBuilderTest {

    @Test
    public void build_with_success() {
        final TransactionRequest request = Fixture.make(TransactionRequest.class);
        final UserEntity sender = Fixture.make(UserEntity.class);
        final UserEntity receiver = Fixture.make(UserEntity.class);
        final TransactionStatus status = TransactionStatus.ERROR;

        final TransactionEntity result = TransactionBuilder.build(request, sender, receiver, status);

        assertNotNull(result);
        assertEquals(status, result.getStatus());
        assertEquals(sender, result.getSender());
        assertEquals(receiver, result.getReceiver());
        assertEquals(request.amount(), result.getAmount());
    }
}