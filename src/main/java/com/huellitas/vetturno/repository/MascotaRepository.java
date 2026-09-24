package com.huellitas.vetturno.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.huellitas.vetturno.model.Mascota;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    /** Lista las mascotas y trae a su propietario en la misma consulta para evitar el problema N+1. */
    @EntityGraph(attributePaths = "propietario")
    List<Mascota> findAllByOrderByNombreAsc();
}
