package com.encurtaurl.principal.api.model.DTOs;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.net.URI;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "shortUrl",
        "originalUrl",
        "createdAt"
})
public class EncurtaResponse {

    @JsonProperty("shortUrl")
    private URI urlEncurtada;
    @JsonProperty("originalUrl")
    private URI urlOriginal;
    @JsonProperty("createdAt")
    private String criadoEm;

    @JsonProperty("shortUrl")
    public URI getUrlEncurtada() {
        return urlEncurtada;
    }

    @JsonProperty("shortUrl")
    public void setUrlEncurtada(URI urlEncurtada) {
        this.urlEncurtada = urlEncurtada;
    }

    @JsonProperty("originalUrl")
    public URI getUrlOriginal() {
        return urlOriginal;
    }

    @JsonProperty("originalUrl")
    public void setUrlOriginal(URI urlOriginal) {
        this.urlOriginal = urlOriginal;
    }

    @JsonProperty("createdAt")
    public String getCriadoEm() {
        return criadoEm;
    }

    @JsonProperty("createdAt")
    public void setCriadoEm(String criadoEm) {
        this.criadoEm = criadoEm;
    }

}
