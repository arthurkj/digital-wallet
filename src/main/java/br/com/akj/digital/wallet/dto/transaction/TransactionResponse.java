package br.com.akj.digital.wallet.dto.transaction;

import br.com.akj.digital.wallet.domain.enumeration.TransactionStatus;

public record TransactionResponse(TransactionStatus status) {

}
