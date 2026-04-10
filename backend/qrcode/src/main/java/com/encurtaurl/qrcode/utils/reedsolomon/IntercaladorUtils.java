package com.encurtaurl.qrcode.utils.reedsolomon;

import java.util.List;

public class IntercaladorUtils {

    public static int[] intercalarBlocos(List<int[]> blocosDados, List<int[]> blocosErro) {
        // Lógica simples para obter o tamanho dos blocos
        // A partir da versão 7, os blocos possuem tamanho variável
        int tamanhoBlocoDados = blocosDados.get(0).length;
        int tamanhoBlocoErros = blocosErro.get(0).length;

        int indice = 0;

        int[] blocoIntercalado = new int[tamanhoBlocoDados*blocosDados.size() + tamanhoBlocoErros*blocosErro.size()];

        for (int i = 0; i < tamanhoBlocoDados; i++) {
            for (int[] bloco : blocosDados) {
                blocoIntercalado[indice++] = bloco[i];
            }
        }

        for (int i = 0; i < tamanhoBlocoErros; i++) {
            for (int[] bloco : blocosErro) {
                blocoIntercalado[indice++] = bloco[i];
            }
        }

        return blocoIntercalado;
    }

}
