package com.encurtaurl.qrcode.utils.print;

import com.encurtaurl.qrcode.utils.codificador.auxiliares.Contexto;
import com.encurtaurl.qrcode.utils.print.cor.Cor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Implementa a lógica para imprimir o QRCode.
 *
 * @see
 *   <a href>https://scanova.io/blog/qr-code-structure/</>
 */
public class QRCodePrinter {

    /**
     * Matriz de bits do QRCode.
     */
    private QRCodeCoordenadas qrCodeCoordenadas;

    /**
     * Contexto do QRCode.
     */
    private Contexto contexto;

    public QRCodePrinter(Contexto contexto) {
        this.contexto = contexto;
        this.qrCodeCoordenadas = new QRCodeCoordenadas(contexto);
    }

    public byte[] desenharQRCode() throws IOException {
        int versao = contexto.getParametros().getVersao();
        desenharFindersEDelimitadores(versao);
        desenharTimingPatterns();
        desenharAllignPatterns(versao);
        desenharDarkModule(versao);
        reservarInformacoesDeFormato();
        executarAlgoritmoZigZag(contexto.getCodeWords().toBooleanArray());
        aplicarMascaraZero();
        aplicarInformacoesDeFormato();

        return converterEmImagem();
    }

    private void desenharFindersEDelimitadores(int versao) {
        int[][] coordenadasFinders = QRCodeConfig.getCoordenadasFinders(versao);

        // Desenhando os finders
        // Para cada extremidade, será pintado um finder/delimitador.
        for (int [] arrayCoordenada : coordenadasFinders) {

            int x = arrayCoordenada[0];
            int y = arrayCoordenada[1];

            for (int linha = 0; linha < QRCodeConfig.TAMANHO_FINDER; linha++) {
                for (int coluna = 0; coluna < QRCodeConfig.TAMANHO_FINDER; coluna++) {
                    int coordenadaXQRCode = coluna + x;
                    int coordenadaYQRCode = linha + y;

                    Cor cor = Cor.PRETO;

                    // Anel branco do Finder.
                    boolean isLinhaBranca = (linha == 1 || linha == 5) && (coluna >= 1 && coluna <= 5);
                    boolean isColunaBranca = (coluna == 1 || coluna == 5) && (linha >= 1 && linha <= 5);

                    if (isLinhaBranca || isColunaBranca) {
                        cor = Cor.BRANCO;
                    }

                    qrCodeCoordenadas.pintar(coordenadaYQRCode, coordenadaXQRCode, cor);
                }
            }

            // Desenha os delimitadores
            for (int linha = -1; linha < QRCodeConfig.TAMANHO_FINDER + 1; linha++) {
                for (int coluna = -1; coluna < QRCodeConfig.TAMANHO_FINDER + 1; coluna++) {
                    int coordenadaXQRCode = coluna + x;
                    int coordenadaYQRCode = linha + y;

                    if ((coordenadaXQRCode < 0 || coordenadaXQRCode >= qrCodeCoordenadas.getTamanhoMatriz())) {
                        continue;
                    }

                    if (coordenadaYQRCode < 0 || coordenadaYQRCode >= qrCodeCoordenadas.getTamanhoMatriz()) {
                        continue;
                    }

                    if (qrCodeCoordenadas.isReservado(coordenadaYQRCode, coordenadaXQRCode)) {
                        continue;
                    }

                    qrCodeCoordenadas.pintar(coordenadaYQRCode, coordenadaXQRCode, Cor.BRANCO);
                }
            }
        }
    }

    private void desenharTimingPatterns() {
        for (int i = 0; i < qrCodeCoordenadas.getTamanhoMatriz(); i++) {
            Cor cor = ((i & 1) == 1) ? Cor.BRANCO : Cor.PRETO;

            if (!qrCodeCoordenadas.isReservado(6, i)) {
                qrCodeCoordenadas.pintar(6, i, cor);
            }

            if (!qrCodeCoordenadas.isReservado(i,6)) {
                qrCodeCoordenadas.pintar(i, 6, cor);
            }
        }
    }

    private void desenharAllignPatterns(int versao) {
        int[][] coordenadasAlinhamento = QRCodeConfig.getCoordenadasDeAlinhamento(versao);

        for (int[] array : coordenadasAlinhamento) {
            int centroX = array[0];
            int centroY = array[1];

            // Se o centro do quadrado (5x5) estiver ocupado, não precisa pintar
            // Pula para o próximo.
            if (qrCodeCoordenadas.isReservado(centroX, centroY)) {
                continue;
            }

            for (int linha = -2; linha <= 2; linha++) {
                for (int coluna = -2; coluna <= 2; coluna++) {
                    int coordenadaXQRCode = centroX + coluna;
                    int coordenadaYQRCode = centroY + linha;

                    // Camada 0 e 3 são da cor preta.
                    Cor cor = Cor.PRETO;

                    // Calculando a distância de Chebyshev
                    int camada = Math.max(Math.abs(linha), Math.abs(coluna));

                    if (camada == 1) {
                        cor = Cor.BRANCO;
                    }

                    qrCodeCoordenadas.pintar(coordenadaYQRCode, coordenadaXQRCode, cor);
                }
            }
        }
    }

    private void desenharDarkModule(int versao) {
        int coordenadaY = 4 * versao + 9;
        int coordenadaX = 8;
        qrCodeCoordenadas.pintar(coordenadaY, coordenadaX, Cor.PRETO);
    }

    private void reservarInformacoesDeFormato() {
        int tamanhoMatriz = qrCodeCoordenadas.getTamanhoMatriz();

        int limiteInferior = tamanhoMatriz - 8;
        int limiteSuperior = tamanhoMatriz - 1;
        for (int coluna = 0; coluna < tamanhoMatriz; coluna++) {
            if (qrCodeCoordenadas.isReservado(8, coluna)) {
                continue;
            }

            if (coluna <= 8) {
                qrCodeCoordenadas.pintar(8, coluna, Cor.INFORMACOES_FORMATO);
            }

           if (coluna >= limiteInferior && coluna <= limiteSuperior) {
               qrCodeCoordenadas.pintar(8, coluna, Cor.INFORMACOES_FORMATO);
           };
        }

        for (int linha = 0; linha < tamanhoMatriz; linha++) {
            if (qrCodeCoordenadas.isReservado(linha, 8)) {
                continue;
            }

            if (linha <= 8) {
                qrCodeCoordenadas.pintar(linha, 8, Cor.INFORMACOES_FORMATO);
            }

            if (linha >= limiteInferior && linha <= limiteSuperior) {
                qrCodeCoordenadas.pintar(linha, 8, Cor.INFORMACOES_FORMATO);
            }
        }
    }

    private void executarAlgoritmoZigZag(boolean[] dadosQRCode) {
        int tamanhoMatriz = qrCodeCoordenadas.getTamanhoMatriz();
        int coluna = tamanhoMatriz - 1;
        int linha = tamanhoMatriz - 1;
        int indice = 0;
        boolean subir = true;

        while (coluna >= 0) {
            // Ignorar o Timing Pattern
            if (coluna == 6) {
                coluna -= 1;
            }

            if (!qrCodeCoordenadas.isReservado(linha, coluna)) {
                // Verificando se acabou a matriz de dados
                if (indice < dadosQRCode.length) {
                    qrCodeCoordenadas.pintar(linha, coluna, dadosQRCode[indice++], false);
                }
                else {
                    qrCodeCoordenadas.pintar(linha, coluna, Cor.BRANCO, false);
                }
            }

            int lookahead = coluna - 1;

            if (lookahead >= 0 && !qrCodeCoordenadas.isReservado(linha, lookahead)) {
                if (indice < dadosQRCode.length) {
                    qrCodeCoordenadas.pintar(linha, lookahead, dadosQRCode[indice++], false);
                } else {
                    qrCodeCoordenadas.pintar(linha, lookahead, Cor.BRANCO, false);
                }
            }

            if (subir) {
                linha--;

                if (linha == -1) {
                    linha = 0;
                    subir = false;
                    coluna -= 2;
                }
            } else {
                linha++;

                if (linha == tamanhoMatriz) {
                    linha = tamanhoMatriz - 1;
                    subir = true;
                    coluna -= 2;
                }
            }
        }

    }

    private void aplicarMascaraZero() {
        int tamanhoMatriz = qrCodeCoordenadas.getTamanhoMatriz();
        for (int linha = 0; linha < tamanhoMatriz; linha++) {
            for (int coluna = 0; coluna < tamanhoMatriz; coluna++) {

                if (qrCodeCoordenadas.isReservado(linha, coluna)) {
                    continue;
                }

                int soma = linha + coluna;

                // Se for impar
                if ((soma & 1) == 1) {
                    continue;
                }

                Cor cor = qrCodeCoordenadas.getCor(linha, coluna);

                if (cor == Cor.PRETO) {
                    cor = Cor.BRANCO;
                } else if (cor == Cor.BRANCO) {
                    cor = Cor.PRETO;
                }

                qrCodeCoordenadas.pintar(linha, coluna, cor, false);
            }
        }
    }

    private void aplicarInformacoesDeFormato() {
        int tamanho = qrCodeCoordenadas.getTamanhoMatriz();

        // Mascara 0 (101 010 000 010 010)
        Cor[] coresMascaraZero  = new Cor[] {
                Cor.PRETO, Cor.BRANCO, Cor.PRETO,
                Cor.BRANCO, Cor.PRETO, Cor.BRANCO,
                Cor.BRANCO, Cor.BRANCO, Cor.BRANCO,
                Cor.BRANCO, Cor.PRETO, Cor.BRANCO,
                Cor.BRANCO, Cor.PRETO, Cor.BRANCO
        };

        // O caminho da primeira cópia (Fazendo a curva no Finder Superior Esquerdo)
        int[][] coordenadasCaminhoFinderSuperiorEsquerdo = {
                {8, 0}, {8, 1}, {8, 2}, {8, 3}, {8, 4}, {8, 5},
                {8, 7}, {8, 8}, {7, 8}, {5, 8}, {4, 8}, {3, 8},
                {2, 8}, {1, 8}, {0, 8}
        };

        // O caminho da segunda cópia (Perto dos outros dois Finders)
        int[][] coordenadasCaminhoFindersRestantes = {
                {tamanho - 1, 8}, {tamanho - 2, 8}, {tamanho - 3, 8},
                {tamanho - 4, 8}, {tamanho - 5, 8}, {tamanho - 6, 8}, {tamanho - 7, 8},
                {8, tamanho - 8}, {8, tamanho - 7}, {8, tamanho - 6},
                {8, tamanho - 5}, {8, tamanho - 4}, {8, tamanho - 3},
                {8, tamanho - 2}, {8, tamanho - 1}
        };

        // 15 bits de Informações de Formato
        for (int i = 0; i < 15; i++) {

            int coordenadaY = coordenadasCaminhoFinderSuperiorEsquerdo[i][0];
            int coordenadaX = coordenadasCaminhoFinderSuperiorEsquerdo[i][1];

            qrCodeCoordenadas.preencherInformacoesDeFormato(coordenadaY, coordenadaX, coresMascaraZero[i]);

            coordenadaY = coordenadasCaminhoFindersRestantes[i][0];
            coordenadaX = coordenadasCaminhoFindersRestantes[i][1];

            qrCodeCoordenadas.preencherInformacoesDeFormato(coordenadaY, coordenadaX, coresMascaraZero[i]);

        }

    }

    private byte[] converterEmImagem() throws IOException {
        int escala = 15;
        int alturaImagem = qrCodeCoordenadas.getTamanhoMatriz();
        int larguraImagem = alturaImagem;

        BufferedImage image = new BufferedImage(alturaImagem*escala, larguraImagem*escala, BufferedImage.TYPE_INT_RGB);

        int[] bufferImagem = ((java.awt.image.DataBufferInt) (image.getRaster().getDataBuffer())).getData();

        int rgbPreto = Color.WHITE.getRGB();
        int rgbBranco = Color.BLACK.getRGB();

        for (int y = 0; y < alturaImagem; y++) {
            for (int x = 0; x < larguraImagem; x++) {
                Cor corQRCode = qrCodeCoordenadas.getMatrizQRCode()[y][x];
                int rgbPixel = corQRCode == Cor.PRETO ? rgbBranco : rgbPreto;

                for (int deslocamentoY = 0 ; deslocamentoY < escala ; deslocamentoY++) {
                    for (int deslocamentoX = 0; deslocamentoX < escala; deslocamentoX++) {
                        int pixelY = (y * escala) + deslocamentoY;
                        int pixelX = (x * escala) + deslocamentoX;

                        // Achatamento da matriz para alcançar o endereço na memória RAM
                        bufferImagem[(pixelY * (larguraImagem*escala)) + pixelX] = rgbPixel;
                    }
                }
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bufferImagem.length);
        ImageIO.write(image, "png", outputStream);
        return outputStream.toByteArray();
    }
}
