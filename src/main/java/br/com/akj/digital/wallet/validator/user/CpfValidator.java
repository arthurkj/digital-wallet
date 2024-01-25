package br.com.akj.digital.wallet.validator.user;

import static br.com.akj.digital.wallet.errors.Error.INVALID_CPF;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.stereotype.Component;

import br.com.akj.digital.wallet.exception.BusinessErrorException;
import br.com.akj.digital.wallet.helper.MessageHelper;
import br.com.caelum.stella.validation.CPFValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CpfValidator {

    private final MessageHelper messageHelper;
    private final CPFValidator stellaCpfvalidator;

    public void validar(final String cpf) {
        if (isBlank(cpf) || isFalse(stellaCpfvalidator.invalidMessagesFor(cpf).isEmpty())) {
            throw new BusinessErrorException(INVALID_CPF, messageHelper.get(INVALID_CPF));
        }
    }
}