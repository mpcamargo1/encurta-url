package com.encurtaurl.principal.api.service;

import com.encurtaurl.principal.api.repository.EncurtaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncurtaService {

    @Autowired
    private EncurtaRepository encurtaRepository;

}
