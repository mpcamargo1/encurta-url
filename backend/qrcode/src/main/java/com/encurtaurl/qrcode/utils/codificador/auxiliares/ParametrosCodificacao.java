package com.encurtaurl.qrcode.utils.codificador.auxiliares;

public class ParametrosCodificacao {

    private final int versao;
    private final int tamanhoMaximoCaracteres;
    private final int tamanhoCodeWordsDados;
    private final int tamanhoCodeWordsTotal;
    private final int tamanhoCodeWordsErro;
    private final int bitsPorCaractere;

    public ParametrosCodificacao(int versao, int tamanhoMaximoCaracteres, int tamanhoCodeWordsDados,
                                 int tamanhoCodeWordsErro) {
        this.versao = versao;
        this.tamanhoMaximoCaracteres = tamanhoMaximoCaracteres;
        this.tamanhoCodeWordsTotal = tamanhoCodeWordsDados + tamanhoCodeWordsErro;
        this.tamanhoCodeWordsDados = tamanhoCodeWordsDados;
        this.tamanhoCodeWordsErro = tamanhoCodeWordsErro;
        this.bitsPorCaractere = 8;
    }

    public int getVersao() {
        return versao;
    }

    public int getTamanhoMaximoCaracteres() {
        return tamanhoMaximoCaracteres;
    }

    public int getTamanhoCodeWordsDados() {
        return tamanhoCodeWordsDados;
    }

    public int getTamanhoCodeWordsTotal() {
        return tamanhoCodeWordsTotal;
    }

    public int getTamanhoCodeWordsErro() {
        return tamanhoCodeWordsErro;
    }

    public int getBitsPorCaractere() {
        return bitsPorCaractere;
    }
}
