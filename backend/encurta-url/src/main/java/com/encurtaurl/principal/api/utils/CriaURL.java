package com.encurtaurl.principal.api.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) // Apenas para sinalizar que é um Singleton
public class CriaURL {

    @Value("${encurtaurl.dominio}")
    private String dominio;

    @Value("${encurtaurl.protocolo}")
    private String protocolo;

    public URI obterURLEncurtada(String hash) {
        StringBuilder builder = new StringBuilder();

        builder.append(protocolo);
        builder.append("://");
        builder.append(dominio);
        builder.append("/");
        builder.append(hash);

        return URI.create(builder.toString());
    }
}
