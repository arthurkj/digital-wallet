package br.com.akj.digital.wallet.dto.transaction;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TransactionRequest(
    @NotNull
    Long receiver,
    @NotNull
    Long sender,
    @Min(1L)
    BigDecimal amount
) {

}
