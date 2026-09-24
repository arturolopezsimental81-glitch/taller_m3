package com.huellitas.vetturno.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.huellitas.vetturno.dto.AuthResponse;
import com.huellitas.vetturno.dto.LoginRequest;
import com.huellitas.vetturno.dto.RegistroRequest;
import com.huellitas.vetturno.exception.ReglaNegocioException;
import com.huellitas.vetturno.model.Rol;
import com.huellitas.vetturno.model.Usuario;
import com.huellitas.vetturno.repository.UsuarioRepository;
import com.huellitas.vetturno.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtService jwtService =
            new JwtService("clave-de-prueba-vetturno-con-mas-de-32-caracteres", 3_600_000L);

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(usuarioRepository, passwordEncoder, authenticationManager, jwtService);
    }

    private RegistroRequest registro(String email, String password) {
        RegistroRequest request = new RegistroRequest();
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }

    @Test
    void registroAsignaUserYGuardaHashBcrypt() {
        when(usuarioRepository.existsByEmail("paula@huellitas.com")).thenReturn(false);

        AuthResponse respuesta = authService.registrar(registro("  Paula@Huellitas.com ", "clave-segura-1"));

        ArgumentCaptor<Usuario> guardado = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(guardado.capture());
        Usuario usuario = guardado.getValue();
        assertThat(usuario.getEmail()).isEqualTo("paula@huellitas.com");
        assertThat(usuario.getRol()).isEqualTo(Rol.USER);
        assertThat(usuario.getPassword()).isNotEqualTo("clave-segura-1").startsWith("$2");
        assertThat(passwordEncoder.matches("clave-segura-1", usuario.getPassword())).isTrue();
        assertThat(jwtService.extraerEmail(respuesta.getToken())).isEqualTo("paula@huellitas.com");
    }

    @Test
    void registroRechazaEmailDuplicado() {
        when(usuarioRepository.existsByEmail("paula@huellitas.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.registrar(registro("paula@huellitas.com", "clave-segura-1")))
                .isInstanceOf(ReglaNegocioException.class);

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void loginValidoEntregaToken() {
        Usuario marta = new Usuario("marta@huellitas.com", passwordEncoder.encode("clave-admin-1"), Rol.ADMIN);
        when(usuarioRepository.findByEmail("marta@huellitas.com")).thenReturn(Optional.of(marta));
        LoginRequest request = new LoginRequest();
        request.setEmail("marta@huellitas.com");
        request.setPassword("clave-admin-1");

        AuthResponse respuesta = authService.login(request);

        assertThat(jwtService.esValido(respuesta.getToken())).isTrue();
    }

    @Test
    void loginInvalidoNoEntregaToken() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));
        LoginRequest request = new LoginRequest();
        request.setEmail("paula@huellitas.com");
        request.setPassword("incorrecta");

        assertThatThrownBy(() -> authService.login(request)).isInstanceOf(BadCredentialsException.class);

        verify(usuarioRepository, never()).findByEmail(any());
    }
}
