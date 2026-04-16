package com.encurtaurl.qrcode.utils.print;

import com.encurtaurl.qrcode.utils.codificador.auxiliares.Contexto;
import com.encurtaurl.qrcode.utils.print.cor.Cor;

public class QRCodeCoordenadas {
    private int versao;
    private Cor[][] matrizQRCode;
    private boolean[][] reservado;

    public QRCodeCoordenadas(Contexto contexto) {
        versao = contexto.getParametros().getVersao();
        int tamanhoMatriz = QRCodeConfig.getTamanho(contexto.getParametros().getVersao());
        this.matrizQRCode = new Cor[tamanhoMatriz][tamanhoMatriz];
        this.reservado = new boolean[tamanhoMatriz][tamanhoMatriz];
    }

    public void pintar(int linha, int coluna, boolean cor, boolean isReservado) {
        if (cor) {
            pintar(linha, coluna, Cor.PRETO, isReservado);
        } else {
            pintar(linha, coluna, Cor.BRANCO, isReservado);
        }
    }

    public void pintar(int linha, int coluna, Cor cor) {
        pintar(linha, coluna, cor, true);
    }

    public void pintar(int linha, int coluna, Cor cor, boolean isReservado) {
        if (reservado[linha][coluna]) {
            throw new RuntimeException("Espaço reservado. Impossível pintar");
        }

        matrizQRCode[linha][coluna] = cor;
        reservado[linha][coluna] = isReservado;
    }

    public void preencherInformacoesDeFormato(int linha, int coluna, Cor cor) {
        Cor corMatrizQRCode = matrizQRCode[linha][coluna];

        if (corMatrizQRCode != Cor.INFORMACOES_FORMATO) {
            throw new RuntimeException("Espaço não representa uma informação de formato");
        }

        matrizQRCode[linha][coluna] = cor;
    }

    public boolean isReservado(int linha, int coluna) {
        return reservado[linha][coluna];
    }

    public int getTamanhoMatriz() {
        return matrizQRCode.length;
    }

    public Cor getCor(int linha, int coluna) {
       return matrizQRCode[linha][coluna];
    }

    public Cor[][] getMatrizQRCode() {
        return matrizQRCode;
    }

    public void imprimirNoConsole() {
        System.out.println("\nQRCode - Impressão");
        System.out.print("  - Versão:" + versao + "\n");

        int tamanho = matrizQRCode.length;

        for (int linha = 0; linha < tamanho; linha++) {
            for (int coluna = 0; coluna < tamanho; coluna++) {
                String pixel = "▒▒"; // Assume que a área não foi desenhada

                if (matrizQRCode[linha][coluna] == Cor.PRETO) {
                    pixel = "██";
                } else if (matrizQRCode[linha][coluna] == Cor.BRANCO) {
                    pixel = "  ";
                } else if (matrizQRCode[linha][coluna] == Cor.INFORMACOES_FORMATO){
                    pixel = "░░";
                }

                System.out.print(pixel);
            }
            System.out.println();
        }

        System.out.println("==================================");
    }
}
