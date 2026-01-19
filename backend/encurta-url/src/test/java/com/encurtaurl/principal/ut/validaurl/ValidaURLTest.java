package com.encurtaurl.principal.ut.validaurl;

import com.encurtaurl.principal.api.validacao.url.ValidaURLImpl;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ValidaURLTest {

    private final ValidaURLImpl validaImpl = new ValidaURLImpl();

    @Mock
    private ConstraintValidatorContext contexto;

    @Test
    void deveReconhecerComoURLValida() {
        String url = "https://www.google.com";
        boolean isValida = validaImpl.isValid(url, null);
        Assertions.assertTrue(isValida);
    }

    @Test
    void deveReconhecerComoURLValida_2() {
        String url = "https://www.youtube.com/watch?v=co6WMzDOh1o";
        boolean isValida = validaImpl.isValid(url, contexto);
        Assertions.assertTrue(isValida);
    }

    @Test
    void deveReconhecerComoURLComQueryParamsValida() {
        String url = "https://www.google.com/search?q=test";
        boolean isValida = validaImpl.isValid(url, contexto);
        Assertions.assertTrue(isValida);
    }

    @Test
    void deveReconhecerComoURLHTTPValidaSemSubdominio() {
        String url = "https://google.com";
        boolean isValida = validaImpl.isValid(url, contexto);
        Assertions.assertTrue(isValida);
    }

    @Test
    void deveReconhecerComoURLHTTPValida() {
        String url = "http://www.google.com";
        boolean isValida = validaImpl.isValid(url, contexto);
        Assertions.assertTrue(isValida);
    }

    @Test
    void deveReconhecerComoURLSemProtocoloInvalida() {
        String url = "www.google.com";
        boolean isValida = validaImpl.isValid(url, contexto);
        Assertions.assertFalse(isValida);
    }

    @Test
    void deveReconhecerComoURLSemPontoComInvalida() {
        String url = "https://www.google/search?q=test";
        boolean isValida = validaImpl.isValid(url, contexto);
        Assertions.assertFalse(isValida);
    }
}
