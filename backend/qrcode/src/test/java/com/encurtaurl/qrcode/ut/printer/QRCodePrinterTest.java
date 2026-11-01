package com.encurtaurl.qrcode.ut.printer;

import com.encurtaurl.qrcode.utils.QRCodeUtils;
import com.encurtaurl.qrcode.utils.print.QRCodePrinter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Set;

/**
 * Contém a implementação dos testes unitários referentes à classe {@link QRCodePrinter}.
 */
public class QRCodePrinterTest {

    /**
     * Garante que os pixels da imagem do QRCode gerado contém somente pixels branco ou preto.
     *
     * @param url
     *   URL a ser codificada.
     *
     * @throws Exception
     *   Uma exceção qualquer.
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "https://encurtaurl.net",
            "https://www.youtube.com/watch?v=TRPBY_lxJfE",
            "https://www.youtube.com/watch?v=IPXIgEAGe4U"
    })
    void imagemDeveTerSomentePixelsBrancoEPreto(String url) throws Exception {
        byte[] dadosImagem = QRCodeUtils.gerarQRCode(url);

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(dadosImagem));

        int altura = bufferedImage.getHeight();
        int largura = bufferedImage.getWidth();

        final Set<Integer> coresRGBValidas = Set.of(Color.WHITE.getRGB(), Color.BLACK.getRGB());

        boolean contemSomentePixelsPretoEBranco = true;

        for (int coordenadaX = 0; coordenadaX < largura; coordenadaX++) {
            for (int coordenadaY = 0; coordenadaY < altura; coordenadaY++) {
                int color = bufferedImage.getRGB(coordenadaX, coordenadaY);

                if (!coresRGBValidas.contains(color)) {
                    contemSomentePixelsPretoEBranco = false;
                    break;
                }
            }
        }

        Assertions.assertTrue(contemSomentePixelsPretoEBranco,
                "A imagem não contém somente pixels branco e preto");
    }
}
