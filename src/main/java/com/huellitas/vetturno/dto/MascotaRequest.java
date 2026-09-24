package com.huellitas.vetturno.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Datos que se reciben para registrar una mascota, el propietario llega solo como id.
 */
public class MascotaRequest {

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Size(max = 60, message = "El nombre admite máximo 60 caracteres")
    private String nombre;

    @NotBlank(message = "La especie es obligatoria")
    @Size(max = 40, message = "La especie admite máximo 40 caracteres")
    private String especie;

    @Size(max = 60, message = "La raza admite máximo 60 caracteres")
    private String raza;

    @NotNull(message = "El propietario es obligatorio")
    @Positive(message = "El id del propietario debe ser positivo")
    private Long propietarioId;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public Long getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(Long propietarioId) {
        this.propietarioId = propietarioId;
    }
}
