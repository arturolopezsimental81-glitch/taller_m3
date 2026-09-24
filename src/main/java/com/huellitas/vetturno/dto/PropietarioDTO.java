package com.huellitas.vetturno.dto;

import com.huellitas.vetturno.model.Propietario;

/**
 * Respuesta plana del propietario, no incluye la lista de sus mascotas.
 */
public class PropietarioDTO {

    private final Long id;
    private final String nombre;
    private final String telefono;
    private final String email;

    public PropietarioDTO(Propietario propietario) {
        this.id = propietario.getId();
        this.nombre = propietario.getNombre();
        this.telefono = propietario.getTelefono();
        this.email = propietario.getEmail();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }
}
