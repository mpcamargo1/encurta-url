package com.encurtaurl.qrcode.service;

import com.encurtaurl.qrcode.utils.QRCodeUtils;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

@Service
public class QRCodeService {

    public byte[] criarQRCode(@NotBlank String url) throws Exception {
        return QRCodeUtils.gerarQRCode(url);
    }
}
