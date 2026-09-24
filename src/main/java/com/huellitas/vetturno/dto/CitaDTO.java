package com.huellitas.vetturno.dto;

import java.time.LocalDateTime;

import com.huellitas.vetturno.model.Cita;

/**
 * Respuesta plana de la cita pensada para recepción, muestra el nombre de la mascota, del
 * propietario y del veterinario(con sus ids) sin exponer las entidades.
 */
public class CitaDTO {

    private final Long id;
    private final LocalDateTime fechaHora;
    private final String motivo;
    private final Long mascotaId;
    private final String mascota;
    private final String propietario;
    private final Long veterinarioId;
    private final String veterinario;

    public CitaDTO(Cita cita) {
        this.id = cita.getId();
        this.fechaHora = cita.getFechaHora();
        this.motivo = cita.getMotivo();
        this.mascotaId = cita.getMascota().getId();
        this.mascota = cita.getMascota().getNombre();
        this.propietario = cita.getMascota().getPropietario().getNombre();
        this.veterinarioId = cita.getVeterinario().getId();
        this.veterinario = cita.getVeterinario().getNombre();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public Long getMascotaId() {
        return mascotaId;
    }

    public String getMascota() {
        return mascota;
    }

    public String getPropietario() {
        return propietario;
    }

    public Long getVeterinarioId() {
        return veterinarioId;
    }

    public String getVeterinario() {
        return veterinario;
    }
}
