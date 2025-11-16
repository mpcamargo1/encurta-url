package com.encurtaurl.principal.api.validacao.url;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidaURLImpl.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidaURL {

    String message() default "URL é inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
