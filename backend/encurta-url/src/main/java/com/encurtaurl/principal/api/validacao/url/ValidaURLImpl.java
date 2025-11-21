package com.encurtaurl.principal.api.validacao.url;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ValidaURLImpl implements ConstraintValidator<ValidaURL, String> {
    @Override
    public boolean isValid(String url, ConstraintValidatorContext contexto) {
        if (url == null || url.trim().isBlank()) {
            return false;
        }


        // ^ (início da string)
        // https?:// (protocolo http:// ou https://)
        // (w{3}\.)*+ (subdomínio, caso houver -- eager, ex: www.)
        // [0-9a-zA-Z\.]{2,} (match até o domínio de nível superior, ex: google, meta.stackoverflow)
        // (\.[a-z]{2,}) (o domínio de nível superior: .com)
        // (/.*)? (opcionalmente, qualquer coisa após a barra, como /caminho)
        // $ (fim da string)
        Pattern pattern = Pattern
                .compile("^(https?://)(w{3}\\.)*+([0-9a-zA-Z.]{2,})(\\.[a-z]{2,})(/.*)?$");

        return pattern.matcher(url).matches();
    }
}
