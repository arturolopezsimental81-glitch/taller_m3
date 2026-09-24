package com.huellitas.vetturno.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huellitas.vetturno.model.Propietario;

public interface PropietarioRepository extends JpaRepository<Propietario, Long> {
}
