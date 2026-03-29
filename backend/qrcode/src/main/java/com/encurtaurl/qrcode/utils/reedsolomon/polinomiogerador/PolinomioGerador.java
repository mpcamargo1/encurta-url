package com.encurtaurl.qrcode.utils.reedsolomon.polinomiogerador;

public class PolinomioGerador {
    private final int eccPorBloco;
    private final int quantidadeBlocos;
    private final int tamanhoBloco;
    private final int[] arrayPolinomioGerador;

    public PolinomioGerador(int eccPorBloco, int quantidadeBlocos, int tamanhoBloco, int[] polinomioGerador) {
        this.eccPorBloco = eccPorBloco;
        this.quantidadeBlocos = quantidadeBlocos;
        this.tamanhoBloco = tamanhoBloco;
        this.arrayPolinomioGerador = polinomioGerador;
    }

    public int getEccPorBloco() {
        return eccPorBloco;
    }

    public int getQuantidadeBlocos() {
        return quantidadeBlocos;
    }

    public int getTamanhoBloco() {
        return tamanhoBloco;
    }

    public int[] getArrayPolinomioGerador() {
        return arrayPolinomioGerador;
    }
}
