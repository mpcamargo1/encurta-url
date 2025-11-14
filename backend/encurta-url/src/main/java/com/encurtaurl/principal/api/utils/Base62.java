package com.encurtaurl.principal.api.utils;

import com.encurtaurl.principal.api.exception.NumeroBase62InvalidoException;

import java.util.HashMap;

public class Base62 {
    private static final char[] alfabeto;
    private static final HashMap<Character, Long> mapaInverso;
    private static final int BASE = 62;

    static {
        alfabeto = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
        mapaInverso = new HashMap<>(alfabeto.length);

        for (int i = 0; i < alfabeto.length; i++) {
            mapaInverso.put(alfabeto[i], (long) i);
        }
    }

    public static String codificar(long id) {

        StringBuilder builder = new StringBuilder();

        long quociente = id;

        while (quociente > 0) {
            int indiceAlfabeto = (int) quociente % BASE;
            builder.append(alfabeto[indiceAlfabeto]);

            quociente = quociente / BASE;
        }

        return builder.reverse().toString();
    }

    public static long decodificar(String chaveBase62) {

        if (chaveBase62 == null || chaveBase62.isBlank()) {
            throw new NumeroBase62InvalidoException(chaveBase62);
        }

        char[] chaveBase62Invertida = new StringBuilder(chaveBase62).reverse().toString().toCharArray();

        long acumulador = 0;
        for (int i = 0; i < chaveBase62Invertida.length; i++) {
            char c = chaveBase62Invertida[i];

            Long valorDecimal = mapaInverso.getOrDefault(c, null);

            if (valorDecimal == null) {
                throw new NumeroBase62InvalidoException(chaveBase62);
            }

            acumulador += valorDecimal * (long) Math.pow(BASE, i);
        }

        return acumulador;
    }
}