package com.encurtaurl.principal.api.service;

import com.encurtaurl.principal.api.model.DTOs.EncurtaResponse;
import com.encurtaurl.principal.api.model.entidade.URLEncurtada;
import com.encurtaurl.principal.api.repository.EncurtaRepository;
import com.encurtaurl.principal.api.utils.Base62;
import com.encurtaurl.principal.api.utils.CriaURL;
import com.encurtaurl.principal.api.utils.Snowflake;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.URI;

@Service
public class EncurtaService {

    @Autowired
    private EncurtaRepository encurtaRepository;

    @Autowired
    private CriaURL criaURL;

    @Autowired
    private Snowflake gerador;

    public EncurtaResponse encurtarURL(@NotBlank String urlOriginal) throws Exception {

        String hash = Base62.codificar(gerador.gerarId());
        URI urlEncurtada = criaURL.obterURLEncurtada(hash);
        encurtaRepository.save(new URLEncurtada(hash, urlOriginal));
        return new EncurtaResponse(urlEncurtada, URI.create(urlOriginal));
    }

}
