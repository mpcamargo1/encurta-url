package com.encurtaurl.principal.it;

import com.encurtaurl.principal.api.model.DTOs.EncurtaRequest;
import com.encurtaurl.principal.api.model.DTOs.EncurtaResponse;
import com.encurtaurl.principal.api.model.entidade.URLEncurtada;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class EncurtarUrlApplicationIT extends AbstractIntegrationTest {

    @Test
    @DisplayName("Encurta uma URL do Youtube e realiza a persistência")
    void deveEncurtarComSucesso() throws Exception {
        final String url = "https://www.youtube.com/watch?v=jGxykN8cx7w";

        EncurtaRequest request = new EncurtaRequest();
        request.setUrlOriginal(url);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request));

        String contentResponse = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        EncurtaResponse response = mapper.readValue(contentResponse, EncurtaResponse.class);

        Assertions.assertEquals(request.getUrlOriginal(), response.getUrlOriginal().toString());
        Assertions.assertNotNull(response.getUrlEncurtada());
        Assertions.assertNotNull(response.getUrlEncurtada().getRawPath());

        String hash = response.getUrlEncurtada().getRawPath().replace("/","");
        URLEncurtada urlEncurtada = repository.findAll().get(0);

        Assertions.assertNotNull(urlEncurtada);
        Assertions.assertEquals(hash, urlEncurtada.getHash());
    }

}
