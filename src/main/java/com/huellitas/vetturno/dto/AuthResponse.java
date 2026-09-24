package com.huellitas.vetturno.dto;

/**
 * Respuesta del registro y del login, solo regresa el JWT y nunca la entidad Usuario.
 */
public class AuthResponse {

    private final String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
