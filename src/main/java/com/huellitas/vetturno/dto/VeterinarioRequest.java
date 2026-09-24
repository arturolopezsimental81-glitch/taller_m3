package com.huellitas.vetturno.dto;

/**
 * Datos que se reciben para registrar un veterinario.
 */
public class VeterinarioRequest {

    private String nombre;

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
