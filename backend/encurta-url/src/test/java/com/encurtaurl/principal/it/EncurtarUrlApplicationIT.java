package com.encurtaurl.principal.it;

import com.encurtaurl.principal.api.model.DTOs.EncurtaRequest;
import com.encurtaurl.principal.api.model.DTOs.EncurtaResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Optional;

public class EncurtarUrlApplicationIT extends AbstractIntegrationTest {

    @DisplayName("Deve encurtar diversas URLs e realizar a persistência")
    @ParameterizedTest(name = "Testando URL: {0}")
    @ValueSource(strings = {
            "https://www.youtube.com/watch?v=jGxykN8cx7w",
            "https://www.amazon.com.br/Use-Cabe%C3%A7a-Java-Aprendiz-Programa%C3%A7%C3%A3o/" +
                    "dp/8550819883/ref=sr_1_1?dib=eyJ2IjoiMSJ9.O-Ffr9Sxl_CvWo1cYFT7_uwGrxOeYT5uuzccL0n1X44jVt5x3QtMwe7KJ" +
                    "sWlKiLZUoY-W8cGiGOR7RO6sH8lxV4M9VonPyjhYEdApwGBQmbZMWb_lPytBgSBZcVQp0-JclBnmgtzM_-UY-FZEr-SZHNCFIZ4" +
                    "B1-a-jfFK1IiQON786eJXK6fRlwrnPLXpGNYhPPaquNh7cpkWHgvYzRG71PIoieGVg3cdnh-ZbVZgJ2eK4Nbt0UgoVwID-" +
                    "GmPStxbIn6ZT3Nfqxm-2sDMgshPFMtt-k0WdjvgVKOgGEHeqk.cezAbjS9-4SqZwqrEk5A65maR75THl5MyFyxxSnc-SI" +
                    "&dib_tag=se&keywords=book+java&qid=1765397412&sr=8-1" +
                    "&ufe=app_do%3Aamzn1.fos.6121c6c4-c969-43ae-92f7-cc248fc6181d",
            "https://stackoverflow.blog/2025/12/10/tell-us-what-you-really-really-do-not-want-to-spend-time-working-on/" +
                    "?cb=1"
    })
    void deveEncurtarURLsComSucesso(String urlOriginal) throws Exception {
        // 1. Preparação
        EncurtaRequest request = new EncurtaRequest();
        request.setUrlOriginal(urlOriginal);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request));

        // 2. Execução
        String contentResponse = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        EncurtaResponse response = mapper.readValue(contentResponse, EncurtaResponse.class);

        // 3. Validação da Resposta
        Assertions.assertEquals(urlOriginal, response.getUrlOriginal().toString());
        Assertions.assertNotNull(response.getUrlEncurtada());
        Assertions.assertNotNull(response.getUrlEncurtada().getRawPath());

        // 4. Validação da Persistência
        String hash = response.getUrlEncurtada().getRawPath().replace("/", "");
        Optional<String> urlNoBanco = repository.findURLOriginal(hash);

        Assertions.assertTrue(urlNoBanco.isPresent(), "A URL deveria ter sido salva no banco");
        Assertions.assertEquals(urlOriginal, urlNoBanco.get());
    }
}
