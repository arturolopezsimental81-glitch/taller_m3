package com.huellitas.vetturno.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Reloj de la aplicación, se inyecta en lugar de llamar directo a LocalDateTime.now() para
 * poder probar la regla de fecha futura con una hora fija.
 */
@Configuration
public class ClockConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
