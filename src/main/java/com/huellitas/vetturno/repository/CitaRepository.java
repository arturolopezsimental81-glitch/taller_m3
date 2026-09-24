package com.huellitas.vetturno.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.huellitas.vetturno.model.Cita;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    /** Consulta derivada que revisa si el veterinario ya tiene una cita en esa fecha y hora. */
    boolean existsByVeterinarioIdAndFechaHora(Long veterinarioId, LocalDateTime fechaHora);

    /** Agenda completa ordenada por fecha, trae la mascota, el propietario y el veterinario en la misma consulta. */
    @EntityGraph(attributePaths = {"mascota", "mascota.propietario", "veterinario"})
    List<Cita> findAllByOrderByFechaHoraAsc();

    /** Agenda de un solo veterinario ordenada por fecha. */
    @EntityGraph(attributePaths = {"mascota", "mascota.propietario", "veterinario"})
    List<Cita> findByVeterinarioIdOrderByFechaHoraAsc(Long veterinarioId);
}
