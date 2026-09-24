package com.huellitas.vetturno.dto;

import com.huellitas.vetturno.model.Veterinario;

/**
 * Respuesta plana del veterinario.
 */
public class VeterinarioDTO {

    private final Long id;
    private final String nombre;
    private final String especialidad;

    public VeterinarioDTO(Veterinario veterinario) {
        this.id = veterinario.getId();
        this.nombre = veterinario.getNombre();
        this.especialidad = veterinario.getEspecialidad();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }
}
