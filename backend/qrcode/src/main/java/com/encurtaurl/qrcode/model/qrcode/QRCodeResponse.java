package com.encurtaurl.qrcode.model.qrcode;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class QRCodeResponse {
    private String conteudoBase64;

    public String getConteudoBase64() {
        return conteudoBase64;
    }

    public void setConteudoBase64(String conteudoBase64) {
        this.conteudoBase64 = conteudoBase64;
    }
}
