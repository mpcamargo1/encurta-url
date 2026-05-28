package com.encurtaurl.qrcode.utils.codificador;

import com.encurtaurl.qrcode.utils.codificador.auxiliares.Contexto;
import com.encurtaurl.qrcode.utils.codificador.auxiliares.Modo;
import com.encurtaurl.qrcode.utils.codificador.auxiliares.ParametrosCodificacao;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CodificadorImpl {

    private static final Map<Integer, Contexto> mapaContextoPorVersao;

    private static final String PAD_1 = "11101100";
    private static final String PAD_2 = "00010001";

    public static Contexto codificar(String url) throws Exception {
        Contexto contexto = getContexto(url);
        return codificarURLEmStringBinaria(contexto);
    }

    private static Contexto codificarURLEmStringBinaria(Contexto contexto) {
        CodeWords codeWords = new CodeWords();
        StringBuilder builder = new StringBuilder();

        codeWords.setModo(Modo.BYTE.getValor());

        ParametrosCodificacao parametros = contexto.getParametros();
        String tamanhoURLEmBits = Integer.toBinaryString(contexto.getUrl().length());
        tamanhoURLEmBits = "0".repeat(8 - tamanhoURLEmBits.length()) + tamanhoURLEmBits;

        // TAMANHO URL
        builder.append(tamanhoURLEmBits);
        codeWords.setTamanho(builder.toString());
        builder.setLength(0);

        Function<Byte, String> byteParaStringBinaria = (b) -> {
            StringBuilder sb = new StringBuilder();

            for (int i = parametros.getBitsPorCaractere() - 1; i >=0; i--) {
                sb.append((b >> i) & 1);
            }

            return sb.toString();
        };

        // DADOS URL
        for (byte b : contexto.getUrl().getBytes(StandardCharsets.UTF_8)) {
            builder.append(byteParaStringBinaria.apply(b));
        }

        codeWords.setDados(builder.toString());
        builder.setLength(0);

        int capacidadeTotalBits = parametros.getTamanhoCodeWordsDados() * 8;
        int bitsUsadosAteDados = codeWords.getModo().length() + codeWords.getTamanho().length()
                + codeWords.getDados().length();
        int tamanhoCaracterTerminador = Math.min(4, capacidadeTotalBits - bitsUsadosAteDados);

        // Terminator
        for (int i = 0; i < tamanhoCaracterTerminador; i++) {
            builder.append("0");
        }

        codeWords.setTerminator(builder.toString());
        builder.setLength(0);

        codeWords.setAlinhamentoBit("");

        // ALINHAMENTO DE BIT
        while (codeWords.isAlinhamentoBitNecessario()) {
            codeWords.setAlinhamentoBit(codeWords.getAlinhamentoBit() + "0");
        }

        // Padding
        int quantidadeCodeWordsAtuais =
                (bitsUsadosAteDados + codeWords.getTerminator().length() + codeWords.getAlinhamentoBit().length())/8;
        int quantidadeCodeWordsRestantes = parametros.getTamanhoCodeWordsDados() - quantidadeCodeWordsAtuais;

        for (int i = 0; i < quantidadeCodeWordsRestantes; i++) {
            builder.append((i % 2 == 0) ? PAD_1 : PAD_2);
        }

        codeWords.setPadding(builder.toString());
        builder.setLength(0);

        contexto.setCodeWords(codeWords);
        return contexto;
    }

    private static Contexto getContexto(String url) throws Exception {
        int quantidadeCaracteres = url.getBytes(StandardCharsets.UTF_8).length;

        for (int i = 1; i <= mapaContextoPorVersao.size(); i++) {
            Contexto ctx = mapaContextoPorVersao.get(i);
            int capacidade = ctx.getParametros().getTamanhoMaximoCaracteres();

            if (quantidadeCaracteres > capacidade) {
                continue;
            }

            Contexto novoContexto = new Contexto(ctx.getParametros());
            novoContexto.setUrl(url);
            return novoContexto;
        }

        throw new Exception("Não foi possível encontrar uma versão do QRCode para codificar");
    }

    static {
        mapaContextoPorVersao = new HashMap<>();

        mapaContextoPorVersao.put(1,
                new Contexto(new ParametrosCodificacao(1, 14, 16, 10)));
        mapaContextoPorVersao.put(2,
                new Contexto(new ParametrosCodificacao(2, 26, 28, 16)));
        mapaContextoPorVersao.put(3,
                new Contexto(new ParametrosCodificacao(3, 42, 44, 26)));
        mapaContextoPorVersao.put(4,
                new Contexto(new ParametrosCodificacao(4, 62, 64, 36)));
        mapaContextoPorVersao.put(5,
                new Contexto(new ParametrosCodificacao(5, 84, 86, 48)));
        mapaContextoPorVersao.put(6,
                new Contexto(new ParametrosCodificacao(6, 106, 108, 64)));
        mapaContextoPorVersao.put(7,
                new Contexto(new ParametrosCodificacao(7, 122, 124, 72)));
        mapaContextoPorVersao.put(8,
                new Contexto(new ParametrosCodificacao(8, 152, 154, 88)));
        mapaContextoPorVersao.put(9,
                new Contexto(new ParametrosCodificacao(9, 180, 182, 110)));
    }
}

