package com.encurtaurl.principal.api.controller;

import com.encurtaurl.principal.api.model.DTOs.EncurtaRequest;
import com.encurtaurl.principal.api.model.DTOs.EncurtaResponse;
import com.encurtaurl.principal.api.service.EncurtaService;
import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EncurtaController {

    @Autowired
    private EncurtaService encurtaService;

    @PostMapping("/shorten")
    @Observed(name = "encurtarURL")
    public ResponseEntity<EncurtaResponse> encurtarURL(@Valid @RequestBody EncurtaRequest requisicao) throws Exception {
        EncurtaResponse resposta = encurtaService.encurtarURL(requisicao.getUrlOriginal());

        return ResponseEntity
                .created(resposta.getUrlEncurtada())
                .body(resposta);
    }

    @GetMapping("/{hash}")
    @Observed(name = "redirecionarParaURLOriginal")
    public ResponseEntity<?> redirecionarParaURLOriginal(@PathVariable String hash) throws Exception {
        return encurtaService.buscarURLOriginal(hash);
    }
}