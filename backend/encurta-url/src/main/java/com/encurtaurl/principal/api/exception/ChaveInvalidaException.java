package com.encurtaurl.principal.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ChaveInvalidaException extends RuntimeException{

    public ChaveInvalidaException(String chave) {
        super("A chave " + chave + "está inválida");
    }
}
