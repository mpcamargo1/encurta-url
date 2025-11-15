package com.encurtaurl.principal.api.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Order(0)
public class LimitadorRequisicaoFilter implements Filter {

    @Value("${encurtaurl.limitadorchamada.maximo-requisicao}")
    private int maximoRequisicao;

    @Value("${encurtaurl.limitadorchamada.intervalo}")
    private int intervalo;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest requisicao = (HttpServletRequest) servletRequest;
        HttpServletResponse resposta = (HttpServletResponse) servletResponse;

        String chave = retornaInternetProtocolAplicacaoCliente(requisicao);

        Long contador = redisTemplate.opsForValue().increment(chave);

        if (contador == null || contador == 1) {
            redisTemplate.expire(chave, intervalo, TimeUnit.SECONDS);
            return;
        }

        if (contador >= maximoRequisicao) {
            resposta.setStatus(429); // Status Code: Too many request;
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String retornaInternetProtocolAplicacaoCliente(HttpServletRequest requisicao) {

        String encaminhadoPor = requisicao.getHeader("X-Forwarded-For");

        if (encaminhadoPor != null) {
            return encaminhadoPor.split(",")[0];
        }

        return requisicao.getRemoteAddr();
    }

}
