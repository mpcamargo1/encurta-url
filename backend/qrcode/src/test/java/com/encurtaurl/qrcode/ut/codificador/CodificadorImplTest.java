package com.encurtaurl.qrcode.ut.codificador;

import com.encurtaurl.qrcode.utils.codificador.CodificadorImpl;
import com.encurtaurl.qrcode.utils.codificador.auxiliares.Contexto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.*;

/**
 * Contém a implementação dos testes unitários referentes à classe {@link CodificadorImpl}.
 */
public class CodificadorImplTest {

    /**
     * Garante que cada versão codifica considerando corretamente a versão do QRCode.
     * Em outras palavras, garante que o QRCode não apresenta perda de informações.
     *
     * @throws Exception
     *   Indica que o processo de codificação falhou e não foi possível resgatar a
     *   versão do QRCode gerado.
     */
    @Test
    void verificarVersaoQRCode() throws Exception {
        Map<Integer, List<String>> mapaVersaoPorURL = instanciarMapaVersaoPorURL();

        for (Map.Entry<Integer, List<String>> chaveValor : mapaVersaoPorURL.entrySet()) {
            int versaoEsperada = chaveValor.getKey();

            for (String urlTeste : chaveValor.getValue()) {
                Contexto contexto = CodificadorImpl.codificar(urlTeste);
                Assertions.assertEquals(versaoEsperada, contexto.getParametros().getVersao());
            }
        }
    }

    /**
     * Geração de Strings aleatórias para testes de versão do QRCode.
     *
     * @return
     *   Um mapa cuja chave representa a versão do QRCode e o valor representa
     *   uma String construída de acordo com tamanho máximo permitido (codificação Byte)
     *   da versão.
     */
    private Map<Integer, List<String>> instanciarMapaVersaoPorURL() {
        final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        int[][] limiteInferiorSuperior = new int[][] {
                {1, 14}, {15, 26}, {27, 42}, {43, 62},
                {63, 84}, {85, 106}, {107, 122},
                {123, 152}
        };

        Random geradorAleatorio = new Random();
        StringBuilder builder = new StringBuilder();
        Map<Integer, List<String>> mapaVersaoPorURL = new HashMap<>();
        for (int i = 0; i < limiteInferiorSuperior.length; i++) {

            for (int k = 0; k < limiteInferiorSuperior[i].length; k++) {

                for (int j = 0; j < limiteInferiorSuperior[i][k]; j++) {
                    int indiceSorteado = geradorAleatorio.nextInt(0, CHAR_POOL.length());
                    builder.append(CHAR_POOL.charAt(indiceSorteado));
                }

                List<String> stringsGeradas = mapaVersaoPorURL
                        .computeIfAbsent(i + 1, k1 -> new ArrayList<>());

                stringsGeradas.add(builder.toString());
                builder.setLength(0);
            }
        }

        return mapaVersaoPorURL;
    }
}
