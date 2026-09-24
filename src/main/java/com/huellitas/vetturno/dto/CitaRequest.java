package com.huellitas.vetturno.dto;

import java.time.LocalDateTime;

/**
 * Datos mínimos para agendar una cita, se recibe la fecha, el motivo y los ids de la mascota y el veterinario.
 */
public class CitaRequest {

    private LocalDateTime fechaHora;

    private String motivo;

    private Long mascotaId;

    private Long veterinarioId;

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Long getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(Long mascotaId) {
        this.mascotaId = mascotaId;
    }

    public Long getVeterinarioId() {
        return veterinarioId;
    }

    public void setVeterinarioId(Long veterinarioId) {
        this.veterinarioId = veterinarioId;
    }
}
