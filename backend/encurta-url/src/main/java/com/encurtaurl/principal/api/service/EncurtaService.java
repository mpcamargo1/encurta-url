package com.encurtaurl.principal.api.service;

import com.encurtaurl.principal.api.model.DTOs.EncurtaResponse;
import com.encurtaurl.principal.api.repository.EncurtaRepository;
import com.encurtaurl.principal.api.utils.Base62;
import com.encurtaurl.principal.api.utils.CriaURL;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EncurtaService {

    @Autowired
    private EncurtaRepository encurtaRepository;

    @Autowired
    private CriaURL criaURL;

    public EncurtaResponse encurtarURL(@NotBlank String urlOriginal) {
        EncurtaResponse resposta = new EncurtaResponse();

        String hash = Base62.codificar(new Random().nextLong(0, Long.MAX_VALUE));

        resposta.setUrlOriginal(urlOriginal);
        resposta.setUrlEncurtada(criaURL.obterURLEncurtada(hash));

        return resposta;
    }

}
