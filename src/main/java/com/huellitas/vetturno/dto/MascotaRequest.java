package com.huellitas.vetturno.dto;

/**
 * Datos que se reciben para registrar una mascota, el propietario llega solo como id.
 */
public class MascotaRequest {

    private String nombre;

    private String especie;

    private String raza;

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
