package com.encurtaurl.qrcode.service;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

@Service
public class QRCodeService {

    public byte[] criarQRCode(@NotBlank String url) throws Exception {
        return new byte[] {};
    }
}
