package com.huellitas.vetturno.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.huellitas.vetturno.dto.CitaDTO;
import com.huellitas.vetturno.dto.CitaRequest;
import com.huellitas.vetturno.service.CitaService;

@RestController
@Tag(name = "Citas", description = "Agenda sin cruces de horario (USER y ADMIN)")
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping
    @Operation(summary = "Agendar una cita futura sin cruce de horario")
    public ResponseEntity<CitaDTO> agendar(@Valid @RequestBody CitaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.agendar(request));
    }

    @GetMapping
    @Operation(summary = "Consultar la agenda completa ordenada por fecha")
    public ResponseEntity<List<CitaDTO>> listar() {
        return ResponseEntity.ok(citaService.listar());
    }

    @GetMapping("/veterinario/{id}")
    @Operation(summary = "Consultar la agenda de un veterinario")
    public ResponseEntity<List<CitaDTO>> listarPorVeterinario(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.listarPorVeterinario(id));
    }
}
