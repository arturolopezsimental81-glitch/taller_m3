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

import com.huellitas.vetturno.dto.VeterinarioDTO;
import com.huellitas.vetturno.dto.VeterinarioRequest;
import com.huellitas.vetturno.service.VeterinarioService;

@RestController
@Tag(name = "Veterinarios", description = "Profesionales de la clínica (alta solo ADMIN)")
@RequestMapping("/api/veterinarios")
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    public VeterinarioController(VeterinarioService veterinarioService) {
        this.veterinarioService = veterinarioService;
    }

    @PostMapping
    @Operation(summary = "Registrar un veterinario (solo ADMIN; USER recibe 403)")
    public ResponseEntity<VeterinarioDTO> crear(@Valid @RequestBody VeterinarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(veterinarioService.crear(request));
    }

    @GetMapping
    @Operation(summary = "Listar veterinarios")
    public ResponseEntity<List<VeterinarioDTO>> listar() {
        return ResponseEntity.ok(veterinarioService.listar());
    }
}
