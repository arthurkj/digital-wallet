package br.com.akj.digital.wallet.resource;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.akj.digital.wallet.dto.transaction.TransactionRequest;
import br.com.akj.digital.wallet.dto.transaction.TransactionResponse;
import br.com.akj.digital.wallet.message.dto.TransactionNotificationMessage;
import br.com.akj.digital.wallet.message.producer.NotificationProducer;
import br.com.akj.digital.wallet.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final NotificationProducer notificationProducer;

    @PostMapping
    public ResponseEntity<TransactionResponse> execute(@RequestBody final TransactionRequest request) {
        return ResponseEntity.ok(transactionService.execute(request));
    }

    @PostMapping("/message")
    public void message() {
        notificationProducer.send(new TransactionNotificationMessage(1L, 2L, BigDecimal.TEN));
    }
}
