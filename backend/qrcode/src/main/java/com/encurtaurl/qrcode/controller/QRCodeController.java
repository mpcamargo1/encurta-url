package com.encurtaurl.qrcode.controller;

import com.encurtaurl.qrcode.model.qrcode.QRCodeRequest;
import com.encurtaurl.qrcode.model.qrcode.QRCodeResponse;
import com.encurtaurl.qrcode.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class QRCodeController {

    @Autowired
    private QRCodeService service;

    @PostMapping("/qrcode")
    public ResponseEntity<QRCodeResponse> codificarEmQRCode(@RequestBody QRCodeRequest request) throws Exception {
        QRCodeResponse response = new QRCodeResponse();
        return ResponseEntity.of(Optional.of(response));
    }

}
