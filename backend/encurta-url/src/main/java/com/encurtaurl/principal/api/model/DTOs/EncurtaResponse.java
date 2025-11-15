package com.encurtaurl.principal.api.model.DTOs;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "shortUrl",
        "originalUrl",
        "createdAt"
})
public class EncurtaResponse {

    @JsonProperty("shortUrl")
    private String urlEncurtada;
    @JsonProperty("originalUrl")
    private String urlOriginal;
    @JsonProperty("createdAt")
    private String criadoEm;

    @JsonProperty("shortUrl")
    public String getUrlEncurtada() {
        return urlEncurtada;
    }

    @JsonProperty("shortUrl")
    public void setUrlEncurtada(String urlEncurtada) {
        this.urlEncurtada = urlEncurtada;
    }

    @JsonProperty("originalUrl")
    public String getUrlOriginal() {
        return urlOriginal;
    }

    @JsonProperty("originalUrl")
    public void setUrlOriginal(String urlOriginal) {
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
