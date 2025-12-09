package com.encurtaurl.principal.ut.base62;

import com.encurtaurl.principal.api.exception.ChaveInvalidaException;
import com.encurtaurl.principal.api.exception.IDInvalidoException;
import com.encurtaurl.principal.api.utils.Base62;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Base62Tests {

    @Test
    void deveCodificarIdentificador10ComSucesso() {
        Assertions.assertEquals("A", Base62.codificar(10));
    }
    @Test
    void deveCodificarIdentificadorMaxIntegerComSucesso() {
        Assertions.assertEquals("2LKcb1", Base62.codificar(Integer.MAX_VALUE));
    }

    @Test
    void deveCodificarIdentificadorMaxLongComSucesso() {
        Assertions.assertEquals("AzL8n0Y58m7", Base62.codificar(Long.MAX_VALUE));
    }

    @Test
    void naoDeveCodificarIDNegativo() {
        Assertions.assertThrows(
                IDInvalidoException.class,
                () -> {Base62.codificar(-2);}
        );
    }

    @Test
    void naoDeveDecodificarChaveInvalida() {
        Assertions.assertThrows(
                ChaveInvalidaException.class,
                () -> {Base62.decodificar("-@#$@Q");}
        );
    }

    @Test
    void naoDeveDecodificarChaveNula() {
        Assertions.assertThrows(
                ChaveInvalidaException.class,
                () -> {Base62.decodificar(null);}
        );
    }

    @Test
    void naoDeveDecodificarChaveVazia() {
        Assertions.assertThrows(
                ChaveInvalidaException.class,
                () -> {Base62.decodificar("");}
        );
    }
}
