package com.encurtaurl.principal.it;
import com.encurtaurl.principal.api.config.JacksonConfig;
import com.encurtaurl.principal.api.repository.EncurtaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

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
    static final GenericContainer<?> redis;

    static {
        cassandra = new CassandraContainer<>("cassandra:4.1")
                .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(240)))
                .withInitScript(".docker/init.cql");

        redis = new GenericContainer<>("redis:7.2-alpine")
                .withExposedPorts(6379)
                .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60)));

        cassandra.start();
        redis.start();
    }

    @DynamicPropertySource
    static void setupProperties(DynamicPropertyRegistry registry) {
        // Cassandra
        registry.add("spring.cassandra.contact-points", cassandra::getHost);
        registry.add("spring.cassandra.port", () -> cassandra.getMappedPort(9042));
        registry.add("spring.cassandra.local-datacenter", cassandra::getLocalDatacenter);

        // Redis
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }
}