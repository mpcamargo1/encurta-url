package com.encurtaurl.principal.api.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObservabilityConfig {

    @Bean
    public ObservedAspect instanciarObservedAspect(ObservationRegistry registro) {
        return new ObservedAspect(registro);
    }
}
