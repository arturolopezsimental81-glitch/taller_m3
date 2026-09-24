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

import com.huellitas.vetturno.dto.PropietarioDTO;
import com.huellitas.vetturno.dto.PropietarioRequest;
import com.huellitas.vetturno.service.PropietarioService;

@RestController
@Tag(name = "Propietarios", description = "Responsables de las mascotas (USER y ADMIN)")
@RequestMapping("/api/propietarios")
public class PropietarioController {

    private final PropietarioService propietarioService;

    public PropietarioController(PropietarioService propietarioService) {
        this.propietarioService = propietarioService;
    }

    @PostMapping
    @Operation(summary = "Registrar un propietario")
    public ResponseEntity<PropietarioDTO> crear(@Valid @RequestBody PropietarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propietarioService.crear(request));
    }

    @GetMapping
    @Operation(summary = "Listar propietarios")
    public ResponseEntity<List<PropietarioDTO>> listar() {
        return ResponseEntity.ok(propietarioService.listar());
    }
}
