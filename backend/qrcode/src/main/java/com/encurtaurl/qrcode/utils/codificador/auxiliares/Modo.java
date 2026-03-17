package com.encurtaurl.qrcode.utils.codificador.auxiliares;

public enum Modo {

    ALFANUMERICO("1000"),
    NUMERICO("0010"),
    BYTE("0100");

    private String valor;

    Modo(String number) {
        this.valor = number;
    }

    public String getValor() {
        return this.valor;
    }
}
