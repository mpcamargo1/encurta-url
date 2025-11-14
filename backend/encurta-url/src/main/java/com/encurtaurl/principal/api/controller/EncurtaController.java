package com.encurtaurl.principal.api.controller;

import com.encurtaurl.principal.api.service.EncurtaService;
import com.encurtaurl.principal.api.utils.Base62;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shorten")
public class EncurtaController {

    @Autowired
    private EncurtaService encurtaService;

    @PostMapping
    public ResponseEntity<String> encurtarURL() {
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
