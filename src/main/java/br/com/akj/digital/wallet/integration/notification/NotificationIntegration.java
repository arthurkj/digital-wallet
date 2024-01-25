package br.com.akj.digital.wallet.integration.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.akj.digital.wallet.integration.notification.dto.NotificationResponse;

@FeignClient(name = "NotificationIntegration", url = "${integration.api.notification.host}${integration.api.notification.v3.basePath}")
public interface NotificationIntegration {

    @GetMapping(value = "${integration.api.notification.v3.send}", consumes = MediaType.APPLICATION_JSON_VALUE)
    NotificationResponse send();
}
