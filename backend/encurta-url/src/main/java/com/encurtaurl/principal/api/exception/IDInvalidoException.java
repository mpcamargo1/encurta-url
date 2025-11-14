package com.encurtaurl.principal.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class IDInvalidoException extends RuntimeException {

    public IDInvalidoException(long id) {
        super("Identificador " + id + "é inválido para codificação");
    }
}
