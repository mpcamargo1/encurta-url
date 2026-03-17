package com.encurtaurl.qrcode.utils;

import com.encurtaurl.qrcode.utils.codificador.CodificadorImpl;
import com.encurtaurl.qrcode.utils.codificador.auxiliares.Contexto;

public class QRCodeUtils {

    public static byte[] gerarQRCode(String url) throws Exception {
        Contexto contexto = CodificadorImpl.codificar(url);

        return new byte[]{};
    }
}
