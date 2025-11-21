package com.encurtaurl.principal.api.controller;

import com.encurtaurl.principal.api.model.DTOs.EncurtaRequest;
import com.encurtaurl.principal.api.model.DTOs.EncurtaResponse;
import com.encurtaurl.principal.api.service.EncurtaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shorten")
public class EncurtaController {

    @Autowired
    private EncurtaService encurtaService;

    @PostMapping
    public ResponseEntity<EncurtaResponse> encurtarURL(@Valid @RequestBody EncurtaRequest requisicao) throws Exception {
        EncurtaResponse resposta = encurtaService.encurtarURL(requisicao.getUrlOriginal());

        return ResponseEntity
                .created(resposta.getUrlEncurtada())
                .body(resposta);
    }
}