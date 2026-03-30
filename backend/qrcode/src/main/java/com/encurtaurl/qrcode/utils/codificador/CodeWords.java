package com.encurtaurl.qrcode.utils.codificador;

public class CodeWords {

    private String modo;
    private String tamanho;
    private String dados;
    private String terminator;
    private String alinhamentoBit;
    private String padding;

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getDados() {
        return dados;
    }

    public void setDados(String dados) {
        this.dados = dados;
    }

    public String getTerminator() {
        return terminator;
    }

    public void setTerminator(String terminator) {
        this.terminator = terminator;
    }

    public String getAlinhamentoBit() {
        return alinhamentoBit;
    }

    public void setAlinhamentoBit(String alinhamentoBit) {
        this.alinhamentoBit = alinhamentoBit;
    }

    public String getPadding() {
        return padding;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public int[] getDadosCodeWords() {
        String dadosBinario = new StringBuilder()
                .append(getModo())
                .append(getTamanho())
                .append(getDados())
                .append(getTerminator())
                .append(getAlinhamentoBit())
                .append(getPadding())
                .toString();

        int[] dados = new int[dadosBinario.length() / 8];

        int indice = 0;
        for (int i = 0; i < dadosBinario.length(); i += 8) {
            String valorByte = dadosBinario.substring(i, i + 8);
            dados[indice++] = Integer.parseInt(valorByte, 2);
        }

        return dados;
    }

    public boolean isAlinhamentoBitNecessario() {
        int total = modo.length() + tamanho.length() + dados.length() + terminator.length() + alinhamentoBit.length();

        if (total % 8 == 0) {
            return false;
        }

        return true;
    }
}
