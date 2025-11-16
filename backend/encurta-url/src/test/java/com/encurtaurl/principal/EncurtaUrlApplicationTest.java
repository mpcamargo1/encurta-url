package com.encurtaurl.principal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;

import java.time.Duration;

@SpringBootTest
public class EncurtaUrlApplicationTest {

    @Container
    public static CassandraContainer<?> cassandra =
            new CassandraContainer<>("cassandra:5.0.6")
                    .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(240)));

    @Container
    @SuppressWarnings("resource")
    public static GenericContainer<?> redis =
            new GenericContainer<>("redis:latest")
                    .withExposedPorts(6379)
                    .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60)));

    static {
        redis.start();
        cassandra.start();
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

    @Test
    void contextLoad() {

    }
}
