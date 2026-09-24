package com.huellitas.vetturno.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huellitas.vetturno.model.Veterinario;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {
}
