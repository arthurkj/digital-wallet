package br.com.akj.digital.wallet.entity;

import br.com.akj.digital.wallet.domain.UserEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserEntityTest {

    @Test
    public void increaseBalance_with_success() {
        final UserEntity user = UserEntity.builder()
                .balance(BigDecimal.ONE)
                .build();

        final BigDecimal amount = BigDecimal.ONE;
        final BigDecimal expectedBalance = BigDecimal.TWO;

        user.increaseBalance(amount);

        assertEquals(expectedBalance, user.getBalance());
    }

    @Test
    public void increaseBalance_with_success_when_balance_is_null() {
        final UserEntity user = UserEntity.builder()
                .build();

        final BigDecimal amount = BigDecimal.ONE;
        final BigDecimal expectedBalance = BigDecimal.ONE;

        user.increaseBalance(amount);

        assertEquals(expectedBalance, user.getBalance());
    }

    @Test
    public void decreaseBalance_with_success() {
        final UserEntity user = UserEntity.builder()
                .balance(BigDecimal.TWO)
                .build();

        final BigDecimal amount = BigDecimal.ONE;
        final BigDecimal expectedBalance = BigDecimal.ONE;

        user.decreaseBalance(amount);

        assertEquals(expectedBalance, user.getBalance());
    }

    @Test
    public void decreaseBalance_with_success_when_balance_is_null() {
        final UserEntity user = UserEntity.builder()
                .build();

        final BigDecimal amount = BigDecimal.ONE;
        final BigDecimal expectedBalance = new BigDecimal("-1");

        user.decreaseBalance(amount);

        assertEquals(expectedBalance, user.getBalance());
    }
}
