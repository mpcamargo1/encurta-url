package com.encurtaurl.principal.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class NumeroBase62InvalidoException extends RuntimeException{

    public NumeroBase62InvalidoException(String chave) {
        super("A chave " + chave + "está inválida");
    }
}
