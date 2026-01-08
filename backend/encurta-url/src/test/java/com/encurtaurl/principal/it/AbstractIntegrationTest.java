package com.encurtaurl.principal.it;
import com.encurtaurl.principal.api.config.JacksonConfig;
import com.encurtaurl.principal.api.repository.EncurtaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Import(JacksonConfig.class)
@EnableCassandraRepositories(basePackages = "com.encurtaurl.principal.api.repository")
public abstract class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    protected EncurtaRepository repository;

    static final CassandraContainer<?> cassandra;
    static final GenericContainer<?> redisSnowflake;
    static final GenericContainer<?> redisURL;

    static {
        cassandra = new CassandraContainer<>("cassandra:4.1")
                .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(240)))
                .withInitScript(".docker/infrastructure/init.cql");

        redisSnowflake = new GenericContainer<>("redis:7.2-alpine")
                .withExposedPorts(Integer.parseInt(System.getenv("REDIS_PORT")))
                .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));

        redisURL = new GenericContainer<>("redis:7.2-alpine")
                .withExposedPorts(Integer.parseInt(System.getenv("REDIS_PORT")))
                .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));

        cassandra.start();
        redisSnowflake.start();
        redisURL.start();
    }

    @DynamicPropertySource
    static void setupProperties(DynamicPropertyRegistry registry) {
        // Cassandra
        registry.add("spring.cassandra.contact-points", cassandra::getHost);
        registry.add("spring.cassandra.port", () -> cassandra.getMappedPort(
                Integer.parseInt(System.getenv("CASSANDRA_PORT"))));
        registry.add("spring.cassandra.local-datacenter", cassandra::getLocalDatacenter);

        // Redis Snowflake
        registry.add("redis.snowflake.host", redisSnowflake::getHost);
        registry.add("redis.snowflake.port", () -> redisSnowflake.getMappedPort(
                Integer.parseInt(System.getenv("REDIS_PORT"))));

        // Redis URL
        registry.add("redis.url.host", redisURL::getHost);
        registry.add("redis.url.port", () -> redisURL.getMappedPort(
                Integer.parseInt(System.getenv("REDIS_PORT"))));
    }
}