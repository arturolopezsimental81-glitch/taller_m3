package com.huellitas.vetturno.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Cita que une la fecha, el motivo, la mascota y el veterinario, las llaves foráneas
 * mascota_id y veterinario_id se guardan en esta tabla(lado muchos). La restricción única
 * de veterinario con fecha_hora respalda en la base la regla de no cruzar citas que valida CitaService.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        name = "uk_cita_veterinario_fecha_hora",
        columnNames = {"veterinario_id", "fecha_hora"}))
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 255)
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    public Cita() {
    }

    public Cita(LocalDateTime fechaHora, String motivo, Mascota mascota, Veterinario veterinario) {
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.mascota = mascota;
        this.veterinario = veterinario;
    }

    public Long getId() {
        return id;
    }

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

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }
}
