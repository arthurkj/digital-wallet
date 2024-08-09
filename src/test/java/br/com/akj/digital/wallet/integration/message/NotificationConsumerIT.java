package br.com.akj.digital.wallet.integration.message;

import br.com.akj.digital.wallet.fixture.Fixture;
import br.com.akj.digital.wallet.integration.BaseIntegrationTest;
import br.com.akj.digital.wallet.integration.notification.dto.NotificationResponse;
import br.com.akj.digital.wallet.message.dto.TransactionNotificationMessage;
import com.google.gson.Gson;
import feign.Request;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpStatus;

import java.time.Duration;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ExtendWith(OutputCaptureExtension.class)
public class NotificationConsumerIT extends BaseIntegrationTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    static ClientAndServer notificationMockServer;

    @Value("${message.notification.queue.name}")
    String notificationQueue;

    @Value("${integration.api.notification.v3.basePath}${integration.api.notification.v3.send}")
    String notificationPath;

    @BeforeAll
    public static void startServer(@Value("${integration.api.notification.host}") final String notificationHost) {
        notificationMockServer = startClientAndServer(Integer.valueOf(notificationHost.split(":")[2]));
    }

    @AfterAll
    public static void stopServer() {
        notificationMockServer.stop();
    }

    private void createMockForNotification() {
        final NotificationResponse response = new NotificationResponse(TRUE);

        notificationMockServer
                .when(
                        request()
                                .withMethod(Request.HttpMethod.GET.toString())
                                .withPath(notificationPath)
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE),
                        exactly(1))
                .respond(
                        response()
                                .withStatusCode(HttpStatus.OK.value())
                                .withHeaders(
                                        new Header(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                                .withBody(new Gson().toJson(response))
                );
    }

    private void createMockForNotificationError() {
        final NotificationResponse response = new NotificationResponse(FALSE);

        notificationMockServer
                .when(
                        request()
                                .withMethod(Request.HttpMethod.GET.toString())
                                .withPath(notificationPath)
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE),
                        exactly(1))
                .respond(
                        response()
                                .withStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .withHeaders(
                                        new Header(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                                .withBody(new Gson().toJson(response))
                );
    }

    @Test
    public void receive_consume_message_with_success(CapturedOutput output) {
        createMockForNotification();

        final TransactionNotificationMessage message = Fixture.make(TransactionNotificationMessage.class);

        rabbitTemplate.convertAndSend(notificationQueue, message);

        await().atMost(Duration.ofSeconds(5))
                .until(() -> output.getOut().contains("message received"));

        assertTrue(output.getOut().contains("of " + message.amount()));
    }

    @Test
    public void receive_consume_message_and_send_to_plq_after_retries(CapturedOutput output) {
        createMockForNotificationError();

        final TransactionNotificationMessage message = Fixture.make(TransactionNotificationMessage.class);

        rabbitTemplate.convertAndSend(notificationQueue, message);

        await().atMost(Duration.ofSeconds(5))
                .until(() -> output.getOut().contains("Max number of retries has been reached"));

        assertTrue(output.getOut().contains("of " + message.amount()));
        assertTrue(output.getOut().contains("Error when try to notify"));
    }
}
