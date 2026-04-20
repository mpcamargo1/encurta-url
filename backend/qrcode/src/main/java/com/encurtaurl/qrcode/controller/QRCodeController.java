package com.encurtaurl.qrcode.controller;

import com.encurtaurl.qrcode.model.qrcode.QRCodeRequest;
import com.encurtaurl.qrcode.service.QRCodeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QRCodeController {

    @Autowired
    private QRCodeService service;

    @PostMapping("/qrcode")
    public ResponseEntity<byte[]> codificarEmQRCode(@Valid @RequestBody QRCodeRequest request) throws Exception {
        byte[] dadosImagem = service.criarQRCode(request.getUrl());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(dadosImagem.length)
                .body(dadosImagem);
    }

}
