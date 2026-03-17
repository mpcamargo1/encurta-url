package com.encurtaurl.qrcode.utils.codificador.auxiliares;

import com.encurtaurl.qrcode.utils.codificador.CodeWords;

public class Contexto {
    private String url;
    private CodeWords codeWords;
    private final ParametrosCodificacao parametros;

    public Contexto(ParametrosCodificacao parametrosCodificacao) {
        this.parametros = parametrosCodificacao;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ParametrosCodificacao getParametros() {
        return parametros;
    }

    public CodeWords getCodeWords() {
        return codeWords;
    }

    public void setCodeWords(CodeWords codeWords) {
        this.codeWords = codeWords;
    }
}