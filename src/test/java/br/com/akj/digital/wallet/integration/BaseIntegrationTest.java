package br.com.akj.digital.wallet.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    static PostgreSQLContainer<?> postgres;

    @Autowired
    JdbcTemplate jdbcTemplate;

    static {
        postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("db")
            .withUsername("username")
            .withPassword("password");
        postgres.start();
    }

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.username", postgres::getUsername);
    }

    @BeforeEach
    void beforeEach() {
        if (jdbcTemplate != null) {
            jdbcTemplate.execute("DELETE FROM transactions");
            jdbcTemplate.execute("DELETE FROM users");
        }
    }
}
