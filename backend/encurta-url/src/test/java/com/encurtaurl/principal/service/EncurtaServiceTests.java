package com.encurtaurl.principal.service;

import com.encurtaurl.principal.api.repository.EncurtaRepository;
import com.encurtaurl.principal.api.service.EncurtaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class EncurtaServiceTests {

    @Mock
    private EncurtaRepository repository;

    @InjectMocks
    private EncurtaService service;

    @Test
    public void deveRedirecionarParaURLOriginal() throws Exception {
        Optional<String> urlOriginal = Optional.of("https://www.youtube.com/");
        String hash = "95xgOE45k8";

        when(repository.findURLOriginal(hash)).thenReturn(urlOriginal);

        ResponseEntity<?> resposta = service.buscarURLOriginal(hash);

        Assertions.assertEquals(HttpStatus.FOUND, resposta.getStatusCode());
        Assertions.assertEquals(urlOriginal.get(), resposta.getHeaders().getLocation().toString());

    }

    @Test
    public void deveRetornarURLOriginalNaoEncontrada() throws Exception {

        when(repository.findURLOriginal(any(String.class))).thenReturn(Optional.empty());

        ResponseEntity<?> resposta = service.buscarURLOriginal("teste");

        Assertions.assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}