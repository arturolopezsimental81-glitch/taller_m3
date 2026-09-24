package com.huellitas.vetturno.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huellitas.vetturno.dto.AuthResponse;
import com.huellitas.vetturno.dto.LoginRequest;
import com.huellitas.vetturno.dto.RegistroRequest;
import com.huellitas.vetturno.exception.ReglaNegocioException;
import com.huellitas.vetturno.model.Rol;
import com.huellitas.vetturno.model.Usuario;
import com.huellitas.vetturno.repository.UsuarioRepository;
import com.huellitas.vetturno.security.JwtService;

/**
 * Se encarga del registro, del hash con BCrypt, de la autenticación y de generar el JWT.
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /** Registra siempre con rol USER, el primer ADMIN se habilita directamente en la base. */
    @Transactional
    public AuthResponse registrar(RegistroRequest request) {
        String email = normalizar(request.getEmail());
        if (usuarioRepository.existsByEmail(email)) {
            throw new ReglaNegocioException("Ya existe una cuenta registrada con ese email");
        }

        Usuario usuario = new Usuario(email, passwordEncoder.encode(request.getPassword()), Rol.USER);
        usuarioRepository.save(usuario);
        return new AuthResponse(jwtService.generarToken(usuario.getEmail(), usuario.getRol().name()));
    }

    /**
     * Revisa las credenciales mediante el AuthenticationManager de Spring, si no son válidas
     * lanza BadCredentialsException y no se genera token.
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = normalizar(request.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();
        return new AuthResponse(jwtService.generarToken(usuario.getEmail(), usuario.getRol().name()));
    }

    private String normalizar(String email) {
        return email.trim().toLowerCase();
    }
}
