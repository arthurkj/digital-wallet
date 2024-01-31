package br.com.akj.digital.wallet.errors;

import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0001;
import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0002;
import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0003;
import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0004;
import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0005;
import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0006;
import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0007;
import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0008;
import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0009;
import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0010;
import static br.com.akj.digital.wallet.errors.ErrorCode.CODE_0011;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Error {

    INTERNAL_ERROR("internal.error", CODE_0001.getCode(), INTERNAL_SERVER_ERROR),
    INVALID_PARAMETERS("invalid.parameters", CODE_0002.getCode(), NOT_ACCEPTABLE),
    USER_NOT_FOUND("user.not.found", CODE_0003.getCode(), NOT_FOUND),
    INVALID_CPF("invalid.cpf", CODE_0004.getCode(), NOT_ACCEPTABLE),
    INVALID_CNPJ("invalid.cnpj", CODE_0005.getCode(), NOT_ACCEPTABLE),
    NOT_UNIQUE_USER("not.unique.user", CODE_0006.getCode(), NOT_ACCEPTABLE),
    ACTION_NOT_PERMITTED("action.not.permitted", CODE_0007.getCode(), NOT_ACCEPTABLE),
    INSUFFICIENT_BALANCE("insufficient.balance", CODE_0008.getCode(), NOT_ACCEPTABLE),
    CLIENT_NOT_FOUND("client.not.found", CODE_0009.getCode(), NOT_ACCEPTABLE),
    UNAUTHORIZED_TRANSACTION("unaunthorized.transaction", CODE_0010.getCode(), NOT_ACCEPTABLE),
    ERROR_ON_TRANSACTION_NOTIFICATION("error.on.transaction.notification", CODE_0011.getCode(), INTERNAL_SERVER_ERROR);

    private final String messageKey;
    private final String code;
    private final HttpStatus httpStatus;
}
