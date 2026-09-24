package com.huellitas.vetturno.dto;

import com.huellitas.vetturno.model.Mascota;

/**
 * Respuesta plana de la mascota que muestra el id y el nombre de su propietario, sin anidar
 * la entidad completa ni sus citas.
 */
public class MascotaDTO {

    private final Long id;
    private final String nombre;
    private final String especie;
    private final String raza;
    private final Long propietarioId;
    private final String propietarioNombre;

    public MascotaDTO(Mascota mascota) {
        this.id = mascota.getId();
        this.nombre = mascota.getNombre();
        this.especie = mascota.getEspecie();
        this.raza = mascota.getRaza();
        this.propietarioId = mascota.getPropietario().getId();
        this.propietarioNombre = mascota.getPropietario().getNombre();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public String getRaza() {
        return raza;
    }

    public Long getPropietarioId() {
        return propietarioId;
    }

    public String getPropietarioNombre() {
        return propietarioNombre;
    }
}
