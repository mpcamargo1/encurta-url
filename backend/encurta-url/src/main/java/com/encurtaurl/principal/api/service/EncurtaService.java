package com.encurtaurl.principal.api.service;

import com.encurtaurl.principal.api.model.DTOs.EncurtaResponse;
import com.encurtaurl.principal.api.model.entidade.URLEncurtada;
import com.encurtaurl.principal.api.repository.EncurtaRepository;
import com.encurtaurl.principal.api.utils.Base62;
import com.encurtaurl.principal.api.utils.CriaURL;
import com.encurtaurl.principal.api.utils.Snowflake;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.Optional;

@Service
public class EncurtaService {

    @Autowired
    private EncurtaRepository encurtaRepository;

    @Autowired
    private CriaURL criaURL;

    @Autowired
    private Snowflake gerador;

    @Autowired
    @Qualifier("redisURL")
    private RedisTemplate<String, String> redisURL;

    public EncurtaResponse encurtarURL(@NotBlank String urlOriginal) throws Exception {
        String hash = Base62.codificar(gerador.gerarId());
        URI urlEncurtada = criaURL.obterURLEncurtada(hash);
        encurtaRepository.save(new URLEncurtada(hash, urlOriginal));
        redisURL.opsForValue().set(hash, urlOriginal);

        return new EncurtaResponse(urlEncurtada, URI.create(urlOriginal));
    }

    public ResponseEntity<?> buscarURLOriginal(@NotBlank String hash) throws Exception {
        String cache = redisURL.opsForValue().get(hash);

        String urlOriginal = Optional.ofNullable(cache)
                .orElseGet(() -> encurtaRepository.findURLOriginal(hash).orElse(null));

        if (urlOriginal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (cache == null) {
            redisURL.opsForValue().set(hash, urlOriginal);
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", urlOriginal)
                .build();
    }

}
