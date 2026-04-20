package com.encurtaurl.qrcode.utils;

import com.encurtaurl.qrcode.utils.codificador.CodificadorImpl;
import com.encurtaurl.qrcode.utils.codificador.auxiliares.Contexto;
import com.encurtaurl.qrcode.utils.print.QRCodePrinter;
import com.encurtaurl.qrcode.utils.reedsolomon.ReedSolomonUtils;

public class QRCodeUtils {

    public static byte[] gerarQRCode(String url) throws Exception {
        Contexto contexto = CodificadorImpl.codificar(url);
        ReedSolomonUtils.gerarArrayDadosComBytesErro(contexto);
        return new QRCodePrinter(contexto).desenharQRCode();
    }
}
