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
    CODE_0006("DIGITAL_WALLET-0006"),
    CODE_0007("DIGITAL_WALLET-0007"),
    CODE_0008("DIGITAL_WALLET-0008"),
    CODE_0009("DIGITAL_WALLET-0009"),
    CODE_0010("DIGITAL_WALLET-0010"),
    CODE_0011("DIGITAL_WALLET-0011");

    private final String code;
}
