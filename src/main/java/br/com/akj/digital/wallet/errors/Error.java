package br.com.akj.digital.wallet.errors;

import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0001;
import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0002;
import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0003;
import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0004;
import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0005;
import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0006;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Error {

    INTERNAL_ERROR("internal.error", CODE_0001.getCode(), INTERNAL_SERVER_ERROR),
    INVALID_PARAMETERS("invalid.parameters", CODE_0002.getCode(), BAD_REQUEST),
    USER_NOT_FOUND("user.not.found", CODE_0003.getCode(), NOT_FOUND),
    INVALID_CPF("invalid.cpf", CODE_0004.getCode(), BAD_REQUEST),
    INVALID_CNPJ("invalid.cnpj", CODE_0005.getCode(), BAD_REQUEST),
    NOT_UNIQUE_USER("not.unique.user", CODE_0006.getCode(), BAD_REQUEST);

    private final String messageKey;
    private final String code;
    private final HttpStatus httpStatus;
}
