package com.encurtaurl.qrcode.utils.reedsolomon;

import java.util.List;

public class IntercaladorUtils {

    public static int[] intercalarBlocos(List<int[]> blocosDados, List<int[]> blocosErro) {
        int maiorTamanhoBlocoDados = blocosDados.get(0).length;
        int maiorTamanhoBlocoErros = blocosErro.get(0).length;

        int indice = 0;

        int[] blocoIntercalado = new int[maiorTamanhoBlocoDados + maiorTamanhoBlocoErros];

        for (int i = 0; i < maiorTamanhoBlocoDados; i++) {
            for (int[] bloco : blocosDados) {
                blocoIntercalado[indice++] = bloco[i];
            }
        }

        for (int i = 0; i < maiorTamanhoBlocoErros; i++) {
            for (int[] bloco : blocosErro) {
                blocoIntercalado[indice++] = bloco[i];
            }
        }

        return blocoIntercalado;
    }

}
