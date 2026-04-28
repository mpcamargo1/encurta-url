package com.encurtaurl.qrcode.service;

import com.encurtaurl.qrcode.utils.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QRCodeService {

    @Autowired
    public EncurtaURLService encurtaURLService;

    public byte[] criarQRCode(String url) throws Exception {
        String urlEncurtada = encurtaURLService.encurtar(url);
        return QRCodeUtils.gerarQRCode(urlEncurtada);
    }
}
