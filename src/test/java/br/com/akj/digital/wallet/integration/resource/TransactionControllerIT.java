package br.com.akj.digital.wallet.integration.resource;

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.math.BigDecimal;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;

import br.com.akj.digital.wallet.domain.UserEntity;
import br.com.akj.digital.wallet.domain.enumeration.TransactionStatus;
import br.com.akj.digital.wallet.domain.enumeration.UserType;
import br.com.akj.digital.wallet.dto.transaction.TransactionRequest;
import br.com.akj.digital.wallet.dto.transaction.TransactionResponse;
import br.com.akj.digital.wallet.errors.dto.ErrorDTO;
import br.com.akj.digital.wallet.integration.BaseIntegrationTest;
import br.com.akj.digital.wallet.integration.authorizer.dto.AuthorizerStatus;
import br.com.akj.digital.wallet.integration.authorizer.dto.TransactionAuthorizationResponse;
import br.com.akj.digital.wallet.integration.notification.dto.NotificationResponse;
import br.com.akj.digital.wallet.repository.UserRepository;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import feign.Request.HttpMethod;

public class TransactionControllerIT extends BaseIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CPFValidator cpfValidator;

    static ClientAndServer authorizerMockServer;

    @Value("${integration.api.authorizer.v3.basePath}${integration.api.authorizer.v3.authorize}")
    String authorizerPath;

    static ClientAndServer notificationMockServer;

    @Value("${integration.api.notification.v3.basePath}${integration.api.notification.v3.send}")
    String notificationPath;

    @BeforeAll
    public static void startServer(@Value("${integration.api.authorizer.host}") final String authorizerHost,
        @Value("${integration.api.notification.host}") final String notificationHost) {
        authorizerMockServer = startClientAndServer(Integer.valueOf(authorizerHost.split(":")[2]));
        notificationMockServer = startClientAndServer(Integer.valueOf(notificationHost.split(":")[2]));
    }

    @AfterAll
    public static void stopServer() {
        authorizerMockServer.stop();
        notificationMockServer.stop();
    }

    private void createMockForAuthorizerWithAuthorization() {
        final TransactionAuthorizationResponse response = new TransactionAuthorizationResponse(
            AuthorizerStatus.AUTHORIZED.getValue());

        authorizerMockServer
            .when(
                request()
                    .withMethod(HttpMethod.GET.toString())
                    .withPath(authorizerPath)
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

    private void createMockForAuthorizerWithUnauthorization() {
        final TransactionAuthorizationResponse response = new TransactionAuthorizationResponse(
            AuthorizerStatus.UNAUTHORIZED.getValue());

        authorizerMockServer
            .when(
                request()
                    .withMethod(HttpMethod.GET.toString())
                    .withPath(authorizerPath)
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

    private void createMockForNotification() {
        final NotificationResponse response = new NotificationResponse(TRUE);

        notificationMockServer
            .when(
                request()
                    .withMethod(HttpMethod.GET.toString())
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


    @Test
    public void makeTransaction_error_not_autorizated() {
        createMockForAuthorizerWithUnauthorization();

        final BigDecimal senderInitialBalance = BigDecimal.TEN;

        final UserEntity sender = UserEntity.builder()
            .name(RandomStringUtils.random(10))
            .personRegistrationCode(cpfValidator.generateRandomValid())
            .email(RandomStringUtils.random(15))
            .password(RandomStringUtils.randomNumeric(6))
            .type(UserType.COMMON)
            .balance(BigDecimal.TEN)
            .build();

        final BigDecimal receiverInitialBalance = BigDecimal.ZERO;

        final UserEntity receiver = UserEntity.builder()
            .name(RandomStringUtils.random(10))
            .personRegistrationCode(cpfValidator.generateRandomValid())
            .email(RandomStringUtils.random(15))
            .password(RandomStringUtils.randomNumeric(6))
            .type(UserType.COMMON)
            .balance(receiverInitialBalance)
            .build();

        userRepository.saveAll(asList(sender, receiver));

        final BigDecimal amount = BigDecimal.TEN;

        final TransactionRequest request = new TransactionRequest(receiver.getId(), sender.getId(), amount);

        final UriComponentsBuilder uriBuilder = UriComponentsBuilder
            .fromUriString("/v1/transaction");

        final ResponseEntity<ErrorDTO> result = restTemplate.postForEntity(uriBuilder.toUriString(), request,
            ErrorDTO.class);

        assertNotNull(result);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, result.getStatusCode());

        final UserEntity updatedSender = userRepository.findById(sender.getId()).get();
        assertEquals(senderInitialBalance, updatedSender.getBalance());

        final UserEntity updatedReceiver = userRepository.findById(receiver.getId()).get();
        assertEquals(receiverInitialBalance, updatedReceiver.getBalance());
    }

    @Test
    public void makeTransaction() {
        createMockForAuthorizerWithAuthorization();
        createMockForNotification();

        final BigDecimal senderInitialBalance = BigDecimal.TEN;

        final UserEntity sender = UserEntity.builder()
            .name(RandomStringUtils.random(10))
            .personRegistrationCode(cpfValidator.generateRandomValid())
            .email(RandomStringUtils.random(15))
            .password(RandomStringUtils.randomNumeric(6))
            .type(UserType.COMMON)
            .balance(senderInitialBalance)
            .build();

        final BigDecimal receiverInitialBalance = BigDecimal.ZERO;

        final UserEntity receiver = UserEntity.builder()
            .name(RandomStringUtils.random(10))
            .personRegistrationCode(cpfValidator.generateRandomValid())
            .email(RandomStringUtils.random(15))
            .password(RandomStringUtils.randomNumeric(6))
            .type(UserType.COMMON)
            .balance(receiverInitialBalance)
            .build();

        userRepository.saveAll(asList(sender, receiver));

        final BigDecimal amount = BigDecimal.TEN;

        final TransactionRequest request = new TransactionRequest(receiver.getId(), sender.getId(), amount);

        final UriComponentsBuilder uriBuilder = UriComponentsBuilder
            .fromUriString("/v1/transaction");

        final ResponseEntity<TransactionResponse> result = restTemplate.postForEntity(uriBuilder.toUriString(), request,
            TransactionResponse.class);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(TransactionStatus.DONE, result.getBody().status());

        final BigDecimal senderExpectedBalance = senderInitialBalance.subtract(amount);
        final BigDecimal receiverExpectedBalance = receiverInitialBalance.add(amount);

        final UserEntity updatedSender = userRepository.findById(sender.getId()).get();
        assertEquals(senderExpectedBalance, updatedSender.getBalance());

        final UserEntity updatedReceiver = userRepository.findById(receiver.getId()).get();
        assertEquals(receiverExpectedBalance, updatedReceiver.getBalance());
    }
}
