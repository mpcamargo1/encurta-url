package com.encurtaurl.principal.api.controller;

import com.encurtaurl.principal.api.model.DTOs.EncurtaRequest;
import com.encurtaurl.principal.api.model.DTOs.EncurtaResponse;
import com.encurtaurl.principal.api.service.EncurtaService;
import com.encurtaurl.principal.api.utils.Base62;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/shorten")
public class EncurtaController {

    @Autowired
    private EncurtaService encurtaService;

    @PostMapping
    public ResponseEntity<EncurtaResponse> encurtarURL(@Valid @RequestBody EncurtaRequest requisicao) {
        EncurtaResponse resposta = encurtaService.encurtarURL(requisicao.getUrlOriginal());
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }
}
