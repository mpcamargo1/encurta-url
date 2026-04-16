package com.encurtaurl.qrcode.utils.print;

/**
 * Definições
 */
public class QRCodeConfig {

    /**
     * Tamanho da altura/largura do finder.
     */
    public static final int TAMANHO_FINDER = 7;

    /**
     * Retorna o tamanho da matriz a ser impressa.
     *
     * @param versao
     *   Versao do QRCode a ser impresso.
     *
     * @return
     *   Tamanho da matriz.
     */
    public static int getTamanho(int versao) {
        return 21 + ((versao - 1) * 4);
    }

    /**
     * Retorna a matriz de array das coordenadas de alinhamento.
     *
     * @param versao
     *   Versão do QRCode a ser impresso.
     *
     * @return
     *   Matriz de array das coordenadas de alinhamento.
     */
    public static int[][] getCoordenadasDeAlinhamento(int versao) {
        return switch (versao) {
            case 1 -> new int[][]{};
            case 2 -> new int[][]{
                    {6, 6}, {6, 18},
                    {18, 6}, {18, 18}
            };
            case 3 -> new int[][]{
                    {6, 6}, {6, 22},
                    {22, 6}, {22, 22}
            };
            case 4 -> new int[][]{
                    {6, 6}, {6, 26},
                    {26, 6}, {26, 26}
            };
            case 5 -> new int[][]{
                    {6, 6}, {6, 30},
                    {30, 6}, {30, 30}
            };
            case 6 -> new int[][]{
                    {6, 6}, {6, 34},
                    {34, 6}, {34, 34}
            };
            case 7 -> new int[][]{
                    {6, 6}, {6, 22}, {6, 38},
                    {22, 6}, {22, 22}, {22, 38},
                    {38, 6}, {38, 22}, {38, 38}
            };
            default -> throw new UnsupportedOperationException("Versão " + versao + " não implementada");
        };
    }

    /**
     * Retorna a matriz que determina a posição dos finders.
     *
     * @param versao
     *   Versão do QRCode a ser impresso
     *
     * @return
     *   A matriz com a posição (x, y) dos finders.
     */
    public static int[][] getCoordenadasFinders(int versao)  {
        int tamanho = getTamanho(versao);
        return new int[][] {
                {0,0}, // Limite superior esquerdo
                {tamanho - TAMANHO_FINDER, 0}, // Limite superior direito
                {0, tamanho - TAMANHO_FINDER} // Limite inferior esquerdo
        };
    }
}