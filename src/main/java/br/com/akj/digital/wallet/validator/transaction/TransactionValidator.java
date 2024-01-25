package br.com.akj.digital.wallet.validator.transaction;

import static br.com.akj.digital.wallet.errors.Error.ACTION_NOT_PERMITTED;
import static br.com.akj.digital.wallet.errors.Error.INSUFFICIENT_BALANCE;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.domain.enumeration.UserType;
import br.com.akj.digital.wallet.exception.BusinessErrorException;
import br.com.akj.digital.wallet.helper.MessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionValidator {

    private final MessageHelper messageHelper;

    public void validate(final UserEntity sender, final Long receiverId, final BigDecimal amount) {
        if (sender.getId().equals(receiverId)) {
            throw new BusinessErrorException(ACTION_NOT_PERMITTED, messageHelper.get(ACTION_NOT_PERMITTED));
        }

        if (UserType.STORE_OWNER.equals(sender.getType())) {
            throw new BusinessErrorException(ACTION_NOT_PERMITTED, messageHelper.get(ACTION_NOT_PERMITTED));
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new BusinessErrorException(INSUFFICIENT_BALANCE, messageHelper.get(INSUFFICIENT_BALANCE));
        }
    }
}
