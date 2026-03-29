package com.encurtaurl.qrcode.utils.reedsolomon.polinomiogerador;

import java.util.HashMap;
import java.util.Map;

public class PolinomioGeradorUtils {

    private static final Map<Integer, PolinomioGerador> mapaPolinomioPorVersao = new HashMap<>();

    static {
        mapaPolinomioPorVersao.put(1, new PolinomioGerador(10, 1, 16,
                new int[] {1, 216, 194, 159, 111, 199, 94, 95, 113, 157, 193}));
        mapaPolinomioPorVersao.put(2, new PolinomioGerador(16, 1, 28,
                new int[] {1, 59, 13, 104, 189, 68, 209, 30, 8, 163, 65, 41, 229, 98, 50, 36, 59}));
        mapaPolinomioPorVersao.put(3, new PolinomioGerador(26, 1, 44,
                new int[] {1, 246, 51, 183, 4, 136, 98, 199, 152, 77, 56, 206, 24, 145, 40, 209, 117, 233, 42, 135, 68,
                        70, 144, 146, 77, 43, 94}));
        mapaPolinomioPorVersao.put(4, new PolinomioGerador(18, 2, 64,
                new int[] {1, 239, 251, 183, 113, 149, 175, 199, 215, 240, 220, 73, 82, 173, 75, 32, 67, 217, 146}));
        mapaPolinomioPorVersao.put(5, new PolinomioGerador(24, 2, 86,
                new int[] {1, 122, 118, 169, 70, 178, 237, 216, 102, 115, 150, 229, 73, 130, 72, 61, 43, 206, 1, 237,
                        247, 127, 217, 144, 117}));
        mapaPolinomioPorVersao.put(6, new PolinomioGerador(16, 4, 108,
                new int[] {1, 59, 13, 104, 189, 68, 209, 30, 8, 163, 65, 41, 229, 98, 50, 36, 59}));
        mapaPolinomioPorVersao.put(7, new PolinomioGerador(18, 4, 136,
                new int[] {1, 239, 251, 183, 113, 149, 175, 199, 215, 240, 220, 73, 82, 173, 75, 32, 67, 217, 146}));
        mapaPolinomioPorVersao.put(8, new PolinomioGerador(22, 4, 154,
                new int[] {1, 89, 179, 131, 176, 182, 244, 19, 189, 69, 40, 28, 137, 29, 123, 67, 253, 86, 218, 230, 26,
                        145, 245}));
        mapaPolinomioPorVersao.put(9, new PolinomioGerador(22, 5, 182,
                new int[] {1, 89, 179, 131, 176, 182, 244, 19, 189, 69, 40, 28, 137, 29, 123, 67, 253, 86, 218, 230, 26,
                145, 245}));
    }

    public static PolinomioGerador getPolinomioGerador(int versao) throws Exception {
        PolinomioGerador polinomioGerador = mapaPolinomioPorVersao.get(versao);

        if (polinomioGerador == null) {
            throw new Exception("Polinômio gerador não encontrada para a versão " + versao);
        }

        return polinomioGerador;
    }

}
