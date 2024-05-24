package br.com.akj.digital.wallet.message.dto;

import java.math.BigDecimal;

public record TransactionNotificationMessage(Long senderId, Long receiverId, BigDecimal amount) {

}
