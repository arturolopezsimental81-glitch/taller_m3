package com.huellitas.vetturno.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void errorInesperadoResponde500SinDetallesInternos() {
        RuntimeException interna = new IllegalStateException("SQL: select * from usuario where password=...");

        ResponseEntity<ApiError> respuesta = handler.manejarGeneral(interna);

        assertThat(respuesta.getStatusCode().value()).isEqualTo(500);
        assertThat(respuesta.getBody().getMensaje())
                .isEqualTo("Ocurrió un error inesperado en el servidor")
                .doesNotContain("SQL", "usuario", "IllegalStateException");
        assertThat(respuesta.getBody().getErrores()).isEmpty();
    }

    @Test
    void reglaDeNegocioResponde400ConSuMensaje() {
        ResponseEntity<ApiError> respuesta = handler.manejarReglaNegocio(
                new ReglaNegocioException("El veterinario Andrés Ruiz ya tiene una cita en ese horario"));

        assertThat(respuesta.getStatusCode().value()).isEqualTo(400);
        assertThat(respuesta.getBody().getStatus()).isEqualTo(400);
        assertThat(respuesta.getBody().getMensaje()).contains("ya tiene una cita");
        assertThat(respuesta.getBody().getTimestamp()).isNotBlank();
    }

    @Test
    void credencialesIncorrectasResponden401SinRevelarCualFallo() {
        ResponseEntity<ApiError> respuesta = handler.manejarCredenciales(new BadCredentialsException("Bad credentials"));

        assertThat(respuesta.getStatusCode().value()).isEqualTo(401);
        assertThat(respuesta.getBody().getMensaje()).isEqualTo("Email o contraseña incorrectos");
    }

    @Test
    void faltaDeRolResponde403() {
        ResponseEntity<ApiError> respuesta = handler.manejarSinPermiso(new AccessDeniedException("Access Denied"));

        assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
    }
}
