package com.encurtaurl.qrcode.utils.reedsolomon;

import com.encurtaurl.qrcode.utils.codificador.auxiliares.Contexto;
import com.encurtaurl.qrcode.utils.galois.CorpoGalois;
import com.encurtaurl.qrcode.utils.reedsolomon.polinomiogerador.PolinomioGerador;
import com.encurtaurl.qrcode.utils.reedsolomon.polinomiogerador.PolinomioGeradorUtils;
import java.util.ArrayList;
import java.util.List;

public class ReedSolomonUtils {

    public static int[] gerarArrayDadosComBytesErro(Contexto contexto) throws Exception {
        int versao = contexto.getParametros().getVersao();

        if (versao > 7) {
            throw new UnsupportedOperationException("Operação não implementada para versão " + versao);
        }

        int[] dadosURL = contexto.getCodeWords().getDadosCodeWords();

        List<int[]> blocosDeDados = new ArrayList<>();
        List<int[]> blocosDeErro = new ArrayList<>();

        PolinomioGerador polinomioGerador = PolinomioGeradorUtils.getPolinomioGerador(
                contexto.getParametros().getVersao());

        int tamanhoBloco = polinomioGerador.getTamanhoBloco();

        for (int i = 0; i < polinomioGerador.getQuantidadeBlocos(); i++) {
            int[] bloco = new int[tamanhoBloco];

            System.arraycopy(dadosURL, (tamanhoBloco * i), bloco, 0, tamanhoBloco);

            blocosDeDados.add(bloco);
            blocosDeErro.add(gerarBytesErroPorBloco(versao, bloco));
        }

        return IntercaladorUtils.intercalarBlocos(blocosDeDados, blocosDeErro);
    }

    private static int[] gerarBytesErroPorBloco(int versao, int[] blocoDados)
            throws Exception {
        PolinomioGerador polinomioGerador = PolinomioGeradorUtils
                .getPolinomioGerador(versao);

        int[] dadosURLMaisBytesErro = new int[blocoDados.length + polinomioGerador.getEccPorBloco()];

        // Copia o conteúdo do array blocoDados.
        System.arraycopy(blocoDados, 0, dadosURLMaisBytesErro, 0, blocoDados.length);

        // Multiplicação e subtração (XOR) polinomial
        for (int i = 0; i < blocoDados.length; i++) {
            int fator = dadosURLMaisBytesErro[i];

            // O coeficiente de índice i é zerado (Apenas por questão de DEBUG - didática)
            dadosURLMaisBytesErro[i] = 0;

            // Multiplicação e subtração (XOR)
            for (int j = 1; j < polinomioGerador.getArrayPolinomioGerador().length; j++) {
                int coeficiente = polinomioGerador.getArrayPolinomioGerador()[j];
                int resultadoProduto = CorpoGalois.multiplicar(fator, coeficiente);
                dadosURLMaisBytesErro[i + j] ^= resultadoProduto;
            }
        }

        int[] bytesErro = new int[polinomioGerador.getEccPorBloco()];

        // Obtem somente os bytes de erro do array resultante
        System.arraycopy(dadosURLMaisBytesErro, blocoDados.length, bytesErro, 0,
                polinomioGerador.getEccPorBloco());

        return bytesErro;
    }

}