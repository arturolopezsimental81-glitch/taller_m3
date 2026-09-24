package com.huellitas.vetturno.dto;

/**
 * Datos de registro, no incluye el rol porque el registro público siempre asigna USER aunque
 * el cliente mande otro valor.
 */
public class RegistroRequest {

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
