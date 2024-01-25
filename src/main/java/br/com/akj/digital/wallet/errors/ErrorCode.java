package br.com.akj.digital.wallet.errors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum ErrorCode {

    CODE_0001("DIGITAL_WALLET-0001"),
    CODE_0002("DIGITAL_WALLET-0002"),
    CODE_0003("DIGITAL_WALLET-0003"),
    CODE_0004("DIGITAL_WALLET-0004"),
    CODE_0005("DIGITAL_WALLET-0005"),
    CODE_0006("DIGITAL_WALLET-0006");

    private final String code;
}
