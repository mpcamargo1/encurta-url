package com.encurtaurl.principal.ut.snowflake;

import com.encurtaurl.principal.api.utils.Snowflake;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SnowflakeTests {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private Snowflake gerador;

    @BeforeEach
    void configurar() throws Exception {
        // 1. O aluguel do container sempre estará ativo
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.setIfAbsent(any(), any(), anyLong(), any())).thenReturn(true);

        // 2. Constrói o objeto Snowflake
        this.gerador = new Snowflake(redisTemplate);

        // 3. Configura a última renovação do aluguel do container
        long timestamp = ReflectionTestUtils.invokeMethod(gerador, "obterTimestamp");
        ReflectionTestUtils.setField(gerador, "ultimoTimestampAluguelRenovado", new AtomicLong(timestamp));
    }

    @Test
    void deveGerarIdentificadorComSucesso() {
        Assertions.assertThatNoException().isThrownBy(() -> gerador.gerarId());
    }

    @Test
    void deveGerarExcecaoPoisGeradorEstaAtrasado() {
        long timestampAdiantadoUmaHora = (long) ReflectionTestUtils.invokeMethod(gerador, "obterTimestamp")
                + 360_000;
        ReflectionTestUtils.setField(gerador, "ultimoTimestampGerado", timestampAdiantadoUmaHora);
        Assertions.assertThatException().isThrownBy(() -> gerador.gerarId());
    }

    @Test
    void deveGerarExcecaoPoisAluguelNaoEValido() {
        ReflectionTestUtils.setField(gerador, "aluguelValido", new AtomicBoolean(false));
        Assertions.assertThatException().isThrownBy(() -> gerador.gerarId()).isInstanceOf(Exception.class);
    }

    @Test
    void deveEsperarParaGerarOutroTimestamp() {
        ReflectionTestUtils.setField(gerador, "sequencia", 4095);
        long ultimoTimestampGerado = (long) ReflectionTestUtils.invokeMethod(gerador, "obterTimestamp");
        ReflectionTestUtils.setField(gerador, "ultimoTimestampGerado", ultimoTimestampGerado);
        long timestampReferencia = (long) ReflectionTestUtils.getField(gerador, "timestampReferencia");

        Instant primeiroInstante = Instant.ofEpochMilli(timestampReferencia + ultimoTimestampGerado);
        Instant novoInstante = primeiroInstante.plusMillis(1);
        try (MockedStatic<Instant> mockedStatic = Mockito.mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(primeiroInstante).thenReturn(novoInstante);

            long id = 0;
            try {id = gerador.gerarId();} catch (Exception ex) {}

            long timestampNovo = id >> 22;
            org.junit.jupiter.api.Assertions.assertNotEquals(0, timestampNovo);
            org.junit.jupiter.api.Assertions.assertNotEquals(ultimoTimestampGerado, timestampNovo);
        }
    }
}
