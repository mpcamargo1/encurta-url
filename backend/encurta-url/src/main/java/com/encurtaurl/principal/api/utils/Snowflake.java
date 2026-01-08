package com.encurtaurl.principal.api.utils;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) // Apenas para sinalizar que é um Singleton
public class Snowflake {

    private final RedisTemplate<String, String> redis;
    private final long timestampReferencia;
    private final int tempoExpiracaoAluguel;
    private final int idContainer;
    private final int MAXIMO_CONTAINER = 1024;
    private AtomicBoolean aluguelValido = new AtomicBoolean(false);
    private AtomicLong ultimoTimestampAluguelRenovado = new AtomicLong(0);
    private int sequencia = 0;
    private long ultimoTimestampGerado;

    public Snowflake(@Qualifier("redisSnowflake") RedisTemplate<String, String> redis)
            throws UnknownHostException {
        this.redis = redis;
        this.tempoExpiracaoAluguel = 30;
        // Época de 2025-01-01 00:00:00 UTC
        this.timestampReferencia = 1735689600000L;
        this.idContainer = criarIDContainer();
    }

    private int criarIDContainer() throws UnknownHostException {
        int hashBusca = obterHashContainer();

        for (int i = 0; i < MAXIMO_CONTAINER; i++) {
            int idContainerAReservar = (hashBusca + i) % MAXIMO_CONTAINER;

            String chaveRedis = construirChaveContainer(idContainerAReservar);

            boolean idAdquirido = redis.opsForValue().setIfAbsent(chaveRedis,
                    String.valueOf(idContainerAReservar), tempoExpiracaoAluguel, TimeUnit.SECONDS);

            if (idAdquirido) {
                aluguelValido.set(true);
                return idContainerAReservar;
            }
        }

        throw new RuntimeException("Não foi possível obter o identificador do container");
    }

    private boolean recuperarIDContainer() {
        String chaveRedis = construirChaveContainer(idContainer);
        boolean chaveRedisAdquirida = redis.opsForValue().setIfAbsent(chaveRedis, String.valueOf(idContainer),
                tempoExpiracaoAluguel, TimeUnit.SECONDS);

        return chaveRedisAdquirida;
    }

    @Scheduled(fixedRate = 10000L)
    public void pulsar() {
        if (!aluguelValido.get()) {
            aluguelValido.set(recuperarIDContainer());
            return;
        }

        renovarAluguel();
    }

    private void renovarAluguel() {
        try {
            String chaveContainer = construirChaveContainer(idContainer);
            boolean chaveRedisAtiva = redis.expire(chaveContainer, tempoExpiracaoAluguel, TimeUnit.SECONDS);
            aluguelValido.set(chaveRedisAtiva);

            if (chaveRedisAtiva) {
                ultimoTimestampAluguelRenovado.set(System.currentTimeMillis());
            }
        } catch (Exception ex) {
            aluguelValido.set(false);
        }
    }

    private int obterHashContainer() throws UnknownHostException {
        String hostName = InetAddress.getLocalHost().getHostName();
        int hash = Math.abs(hostName.hashCode());
        return hash % MAXIMO_CONTAINER;
    }

    public synchronized long gerarId() throws Exception {
        long timestampAtual = obterTimestamp();

        final int maximoSequencia = 4095;  // (1L << 12 ) - 1 => 0111111111111 => 4095

        if (!aluguelValido.get()) {
            throw new Exception("Aluguel não ativo. Falha ao gerar o ID");
        }

        if (timestampAtual - ultimoTimestampAluguelRenovado.longValue()  >= 5000) {
            throw new Exception("Aluguel do container próximo da expiração");
        }

        if (timestampAtual < ultimoTimestampGerado) {
            throw new Exception("Timestamp do sistema está instável");
        }

        if (timestampAtual > ultimoTimestampGerado) {
            sequencia = 0;
        }

        if (timestampAtual == ultimoTimestampGerado) {
            sequencia = (sequencia + 1) & maximoSequencia;

            if (sequencia == 0) {
                timestampAtual = esperarProximoTimestamp(timestampAtual);
            }
        }

        ultimoTimestampGerado = timestampAtual;

        /**
         * Layout do ID Snowflake (64 bits):
         * Estrutura: | BIT DE SINAL | TIMESTAMP | NODE ID | SEQUENCE |
         * Posição:   |      63      | 62 - 22   | 21 - 12 | 11 - 0   |
         * Bits:      |      1       |   41 bits | 10 bits | 12 bits  |
         * -----------------------------------------------------------
         * TOTAL:    |                          64 bits               |
         */
        long idGerado = timestampAtual << 22
                | ((long) idContainer << 12)
                | sequencia;

        return idGerado;
    }

    private long esperarProximoTimestamp(long timestamp) {
        while (timestamp - ultimoTimestampGerado == 0) {
            timestamp = obterTimestamp();
        }
        return timestamp;
    }

    private long obterTimestamp() {
        return Instant.now().toEpochMilli() - timestampReferencia;
    }

    private String construirChaveContainer(long idContainer) {
        return "snowflake:container:" + idContainer;
    }
}
