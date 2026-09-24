package com.huellitas.vetturno.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos que se reciben para registrar un veterinario.
 */
public class VeterinarioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre admite máximo 100 caracteres")
    private String nombre;

    @NotBlank(message = "La especialidad es obligatoria")
    @Size(max = 80, message = "La especialidad admite máximo 80 caracteres")
    private String especialidad;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
