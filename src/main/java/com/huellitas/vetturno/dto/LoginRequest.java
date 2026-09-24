package com.huellitas.vetturno.dto;

/**
 * Email y contraseña para iniciar sesión.
 */
public class LoginRequest {

    private String email;

    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
