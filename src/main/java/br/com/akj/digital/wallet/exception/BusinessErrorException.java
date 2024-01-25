package br.com.akj.digital.wallet.exception;

import br.com.akj.digital.wallet.errors.Error;

public class BusinessErrorException extends AbstractErrorException {

    public BusinessErrorException(final Error error, final String reason) {
        super(error.getHttpStatus(), reason, error.getCode());
    }
}
