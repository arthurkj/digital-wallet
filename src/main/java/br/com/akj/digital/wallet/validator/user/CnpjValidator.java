package br.com.akj.digital.wallet.validator.user;

import static br.com.akj.digital.wallet.errors.Error.INVALID_CNPJ;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.stereotype.Component;

import br.com.akj.digital.wallet.exception.BusinessErrorException;
import br.com.akj.digital.wallet.helper.MessageHelper;
import br.com.caelum.stella.validation.CNPJValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CnpjValidator {

    private final MessageHelper messageHelper;
    private final CNPJValidator stellaCnpjValidator;

    public void validar(final String cnpj) {
        if (isBlank(cnpj) || isFalse(stellaCnpjValidator.invalidMessagesFor(cnpj).isEmpty())) {
            throw new BusinessErrorException(INVALID_CNPJ, messageHelper.get(INVALID_CNPJ));
        }
    }
}