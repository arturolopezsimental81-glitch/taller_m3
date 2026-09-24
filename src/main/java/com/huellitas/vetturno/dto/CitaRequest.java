package com.huellitas.vetturno.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Datos mínimos para agendar una cita, se recibe la fecha, el motivo y los ids de la mascota y el veterinario.
 */
public class CitaRequest {

    @NotNull(message = "La fecha y hora es obligatoria")
    @Future(message = "La fecha y hora de la cita debe ser futura")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 255, message = "El motivo admite máximo 255 caracteres")
    private String motivo;

    @NotNull(message = "La mascota es obligatoria")
    @Positive(message = "El id de la mascota debe ser positivo")
    private Long mascotaId;

    @NotNull(message = "El veterinario es obligatorio")
    @Positive(message = "El id del veterinario debe ser positivo")
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
