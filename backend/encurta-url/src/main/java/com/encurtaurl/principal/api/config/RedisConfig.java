package com.encurtaurl.principal.api.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.time.Duration;

@Configuration
public class RedisConfig {

    // 1. Definição das Propriedades do Snowflake
    @Bean
    @ConfigurationProperties(prefix = "redis.snowflake")
    public RedisInstanceConfig createRedisSnowflakeInstanceConfig() {
        return new RedisInstanceConfig();
    }

    // 2. Definição das Propriedades da URL
    @Bean
    @ConfigurationProperties(prefix = "redis.url")
    public RedisInstanceConfig createRedisURLInstanceConfig() {
        return new RedisInstanceConfig();
    }

    // 3. Função auxiliar para montar a Factory
    private LettuceConnectionFactory buildConnectionFactory(RedisInstanceConfig config) {
        SocketOptions socketOptions = SocketOptions.builder()
                .connectTimeout(Duration.ofMillis(config.getConnectTimeout()))
                .build();

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(config.getTimeout()))
                .clientOptions(ClientOptions.builder().socketOptions(socketOptions).build())
                .build();

        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(config.getHost(), config.getPort());

        LettuceConnectionFactory factory = new LettuceConnectionFactory(serverConfig, clientConfig);
        factory.afterPropertiesSet();
        return factory;
    }

    // 4. Criar Instância Redis para o Snowflake
    @Bean(name = "redisSnowflake")
    public RedisTemplate<String, String> snowflakeTemplate(
            @Qualifier("createRedisSnowflakeInstanceConfig") RedisInstanceConfig config) {
        return createTemplate(buildConnectionFactory(config));
    }

    // 5. Cria Instãncia Redis para as URLs
    @Bean(name = "redisURL")
    public RedisTemplate<String, String> urlTemplate(
            @Qualifier("createRedisURLInstanceConfig") RedisInstanceConfig config) {
        return createTemplate(buildConnectionFactory(config));
    }

    private RedisTemplate<String, String> createTemplate(LettuceConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    public static class RedisInstanceConfig {
        private String host;
        private int port;
        private int timeout;
        private int connectTimeout;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
        }
    }
}
