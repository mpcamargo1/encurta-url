package com.encurtaurl.principal.api.model.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "longUrl",
        "customAlias"
})
public class EncurtaRequest {

    @NotBlank
    @JsonProperty("longUrl")
    private String urlOriginal;
    @JsonProperty("customAlias")
    private String customAlias;

    @JsonProperty("longUrl")
    public String getUrlOriginal() {
        return urlOriginal;
    }

    @JsonProperty("longUrl")
    public void setUrlOriginal(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }

    @JsonProperty("customAlias")
    public String getCustomAlias() {
        return customAlias;
    }

    @JsonProperty("customAlias")
    public void setCustomAlias(String customAlias) {
        this.customAlias = customAlias;
    }

}
