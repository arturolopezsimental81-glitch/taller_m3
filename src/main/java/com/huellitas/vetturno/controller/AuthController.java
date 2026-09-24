package com.huellitas.vetturno.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.huellitas.vetturno.dto.AuthResponse;
import com.huellitas.vetturno.dto.LoginRequest;
import com.huellitas.vetturno.dto.RegistroRequest;
import com.huellitas.vetturno.service.AuthService;

@RestController
@Tag(name = "Autenticación", description = "Registro y login públicos que entregan un JWT")
@SecurityRequirements
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar una cuenta (siempre con rol USER)")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.ok(authService.registrar(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener un JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
