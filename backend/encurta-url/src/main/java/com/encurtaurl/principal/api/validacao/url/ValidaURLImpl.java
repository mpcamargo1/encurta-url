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
        // (w{3}\.) (pelo menos um subdomínio, ex: www.)
        // +[a-zA-Z]{2,} (o domínio de segundo nível, ex: google)
        // (\.{1}[a-z]{2,}) (o domínio de nível superior: .com)
        // (/.*)? (opcionalmente, qualquer coisa após a barra, como /caminho)
        // $ (fim da string)
        Pattern pattern = Pattern.compile("^(https?://)(w{3}\\.)[a-zA-Z]{2,}(\\.{1}[a-z]{2,})(/.*)?$");

        return pattern.matcher(url).matches();
    }
}
