package br.com.akj.digital.wallet.validator.transaction;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils.nextLong;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.domain.enumeration.UserType;
import br.com.akj.digital.wallet.exception.BusinessErrorException;
import br.com.akj.digital.wallet.fixture.Fixture;
import br.com.akj.digital.wallet.helper.MessageHelper;

@ExtendWith(MockitoExtension.class)
class TransactionValidatorTest {

    @InjectMocks
    private TransactionValidator validator;

    @Mock
    private MessageHelper messageHelper;

    @Test
    public void validate_with_success(){
        final UserEntity sender = Fixture.make(UserEntity.class);
        final Long receiverId = sender.getId() + 1L;
        final BigDecimal amount = BigDecimal.TEN;

        sender.setBalance(amount);
        sender.setType(UserType.COMMON);

        validator.validate(sender, receiverId, amount);
    }

    @Test
    public void validate_sender_equals_to_receiver(){
        final UserEntity sender = Fixture.make(UserEntity.class);
        final Long receiverId = sender.getId();
        final BigDecimal amount = BigDecimal.TEN;

        sender.setBalance(amount);
        sender.setType(UserType.COMMON);

        assertThrows(BusinessErrorException.class, () -> validator.validate(sender, receiverId, amount));
    }

    @Test
    public void validate_sender_is_a_store_owner(){
        final UserEntity sender = Fixture.make(UserEntity.class);
        final Long receiverId = sender.getId() + 1L;
        final BigDecimal amount = BigDecimal.TEN;

        sender.setBalance(amount);
        sender.setType(UserType.STORE_OWNER);

        assertThrows(BusinessErrorException.class, () -> validator.validate(sender, receiverId, amount));
    }

    @Test
    public void validate_insufficient_balance(){
        final UserEntity sender = Fixture.make(UserEntity.class);
        final Long receiverId = sender.getId() + 1L;
        final BigDecimal amount = BigDecimal.TEN;

        sender.setBalance(amount.subtract(BigDecimal.ONE));
        sender.setType(UserType.COMMON);

        assertThrows(BusinessErrorException.class, () -> validator.validate(sender, receiverId, amount));
    }
}