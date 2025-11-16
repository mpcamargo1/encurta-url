package com.encurtaurl.principal.limitadorequisicao;

import com.encurtaurl.principal.api.config.LimitadorRequisicaoFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.io.IOException;
import java.lang.reflect.Field;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LimitadorRequisicaoTests {

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private HttpServletRequest requisicao;

    @Mock
    private HttpServletResponse resposta;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private LimitadorRequisicaoFilter filtro;

    private static final String IP_REQUISICAO = "192.168.0.1";
    private static final int MAXIMO_REQUISICAO = 50;
    private static final int INTERVALO = 10;

    @BeforeEach
    void configurar() throws Exception {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        when(requisicao.getRemoteAddr()).thenReturn(IP_REQUISICAO);
        when(requisicao.getHeader("X-Forwarded-For")).thenReturn(null);

        injetarValor(filtro, "maximoRequisicao", MAXIMO_REQUISICAO);
        injetarValor(filtro, "intervalo", INTERVALO);
    }

    @Test
    void bloquearChamadaAcimaDoLimite() throws ServletException, IOException {
        when(valueOperations.increment(IP_REQUISICAO)).thenReturn((long) (MAXIMO_REQUISICAO + 1));

        filtro.doFilter(requisicao, resposta, filterChain);

        verify(resposta, Mockito.times(1)).setStatus(429);
        verify(filterChain, Mockito.never()).doFilter(Mockito.any(), Mockito.any());
    }


    private void injetarValor(Object alvo, String campo, Object valor)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = alvo.getClass().getDeclaredField(campo);

        boolean canAccess = field.canAccess(alvo);
        field.setAccessible(true);
        field.set(alvo, valor);
        field.setAccessible(canAccess);
    }
}
