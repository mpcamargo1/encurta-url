package com.encurtaurl.qrcode.utils.galois;

public class CorpoGalois {

    /**
     * Array lookup para o expoente.
     */
    private static final int[] LOOKUP_EXPOENTE = new int[512];

    /**
     * Array lookup para o logaritmo.
     */
    private static final int[] LOOKUP_LOGARITMO = new int[256];

    /**
     * 285 = 1 0001 1101 = x^8 + x^4 + x^3 + x^2 + x^0
     */
    private static final int POLINOMIO_IRREDUTIVEL = 285;

    /**
     * 256 = 0x100
     */
    private static final int NUM_256_HEXADECIMAL = 0x100;

    static {
        int x = 1;
        for (int i = 0; i < 255; i++) {
            LOOKUP_EXPOENTE[i] = x;
            LOOKUP_LOGARITMO[x] = i;

            // Multiplica por dois
            x = x << 1;

            // Detecção de estouro
            if ((x & NUM_256_HEXADECIMAL) != 0) {
                // XOR com o polinômio irredutível
                x ^= POLINOMIO_IRREDUTIVEL;
            }
        }

        for (int i = 255; i < 512; i++) {
            LOOKUP_EXPOENTE[i] = LOOKUP_EXPOENTE[i - 255];
        }
    }

    public static int multiplicar(int a, int b) {
        // Não existe uma entrada no array lookup de logaritmo para o índice 0;
        if (a == 0 || b == 0) {
            return 0;
        }

        return LOOKUP_EXPOENTE[LOOKUP_LOGARITMO[a] + LOOKUP_LOGARITMO[b]];
    }

}
