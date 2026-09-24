package com.huellitas.vetturno.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.huellitas.vetturno.dto.MascotaDTO;
import com.huellitas.vetturno.dto.MascotaRequest;
import com.huellitas.vetturno.service.MascotaService;

@RestController
@Tag(name = "Mascotas", description = "Pacientes relacionados con su propietario (USER y ADMIN)")
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @PostMapping
    @Operation(summary = "Registrar una mascota de un propietario existente")
    public ResponseEntity<MascotaDTO> crear(@Valid @RequestBody MascotaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mascotaService.crear(request));
    }

    @GetMapping
    @Operation(summary = "Listar mascotas con su propietario")
    public ResponseEntity<List<MascotaDTO>> listar() {
        return ResponseEntity.ok(mascotaService.listar());
    }
}
