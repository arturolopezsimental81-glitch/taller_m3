package com.huellitas.vetturno.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huellitas.vetturno.dto.PropietarioDTO;
import com.huellitas.vetturno.dto.PropietarioRequest;
import com.huellitas.vetturno.service.PropietarioService;

@RestController
@RequestMapping("/api/propietarios")
public class PropietarioController {

    private final PropietarioService propietarioService;

    public PropietarioController(PropietarioService propietarioService) {
        this.propietarioService = propietarioService;
    }

    @PostMapping
    public ResponseEntity<PropietarioDTO> crear(@RequestBody PropietarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propietarioService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<PropietarioDTO>> listar() {
        return ResponseEntity.ok(propietarioService.listar());
    }
}
