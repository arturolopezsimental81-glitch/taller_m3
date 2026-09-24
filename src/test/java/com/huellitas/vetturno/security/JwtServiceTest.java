package com.huellitas.vetturno.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private static final String SECRETO = "clave-de-prueba-vetturno-con-mas-de-32-caracteres";
    private static final long UNA_HORA_MS = 3_600_000L;

    @Test
    void generaTokenValidoConElEmailComoSujeto() {
        JwtService jwtService = new JwtService(SECRETO, UNA_HORA_MS);

        String token = jwtService.generarToken("paula@huellitas.com", "USER");

        assertThat(jwtService.esValido(token)).isTrue();
        assertThat(jwtService.extraerEmail(token)).isEqualTo("paula@huellitas.com");
    }

    @Test
    void rechazaTokenExpirado() {
        JwtService jwtService = new JwtService(SECRETO, -1_000L);

        String token = jwtService.generarToken("paula@huellitas.com", "USER");

        assertThat(jwtService.esValido(token)).isFalse();
    }

    @Test
    void rechazaTokenFirmadoConOtraClave() {
        JwtService otroEmisor = new JwtService("otra-clave-distinta-tambien-de-32-caracteres-o-mas", UNA_HORA_MS);
        JwtService jwtService = new JwtService(SECRETO, UNA_HORA_MS);

        String token = otroEmisor.generarToken("intruso@correo.com", "ADMIN");

        assertThat(jwtService.esValido(token)).isFalse();
    }

    @Test
    void rechazaTextoQueNoEsToken() {
        JwtService jwtService = new JwtService(SECRETO, UNA_HORA_MS);

        assertThat(jwtService.esValido("no-es-un-jwt")).isFalse();
        assertThat(jwtService.esValido("")).isFalse();
    }

    @Test
    void exigeClaveDeAlMenos32Caracteres() {
        assertThatThrownBy(() -> new JwtService("corta", UNA_HORA_MS))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("32");
    }
}
