package br.com.akj.digital.wallet.errors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum ErrorCode {

    CODE_0001("DIGITAL_WALLET-0001"),
    CODE_0002("DIGITAL_WALLET-0002"),
    CODE_0003("DIGITAL_WALLET-0003");

    private final String code;
}
