package br.com.akj.digital.wallet.validator.user;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.akj.digital.wallet.exception.BusinessErrorException;
import br.com.akj.digital.wallet.helper.MessageHelper;
import br.com.caelum.stella.SimpleValidationMessage;
import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;

@ExtendWith(MockitoExtension.class)
class CpfValidatorTest {

    @InjectMocks
    private CpfValidator validator;

    @Mock
    private MessageHelper messageHelper;

    @Mock
    private CPFValidator stellaCpfvalidator;

    @Test
    void validate_with_success() {
        final String cpf = randomNumeric(11);

        when(stellaCpfvalidator.invalidMessagesFor(cpf)).thenReturn(emptyList());

        validator.validate(cpf);

        verify(stellaCpfvalidator).invalidMessagesFor(cpf);
    }

    @Test
    void validate_error_when_blank() {
        final String cpf = "";

        assertThrows(BusinessErrorException.class, () -> validator.validate(cpf));

        verifyNoInteractions(stellaCpfvalidator);
    }

    @Test
    void validate_error_when_invalid() {
        final String cpf = randomNumeric(11);

        final ValidationMessage validationMessage = new SimpleValidationMessage("");

        when(stellaCpfvalidator.invalidMessagesFor(cpf)).thenReturn(singletonList(validationMessage));

        assertThrows(BusinessErrorException.class, () -> validator.validate(cpf));

        verify(stellaCpfvalidator).invalidMessagesFor(cpf);
    }
}