package br.com.akj.digital.wallet.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.function.Predicate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    static PostgreSQLContainer<?> postgres;
    static RabbitMQContainer rabbit;

    @Autowired
    JdbcTemplate jdbcTemplate;

    static {
        postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("db")
            .withUsername("username")
            .withPassword("password");
        postgres.start();

        rabbit = new RabbitMQContainer("rabbitmq:3.8.6-management");
        rabbit.start();
    }

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.username", postgres::getUsername);

        registry.add("spring.rabbitmq.host", rabbit::getHost);
        registry.add("spring.rabbitmq.port", rabbit::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbit::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbit::getAdminPassword);
    }

    @BeforeEach
    void beforeEach() {
        if (jdbcTemplate != null) {
            jdbcTemplate.execute("DELETE FROM transactions");
            jdbcTemplate.execute("DELETE FROM users");
        }
    }
}
