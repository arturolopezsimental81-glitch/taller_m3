package com.huellitas.vetturno.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Reglas de acceso de VetTurno, el registro y el login son públicos, el alta de veterinarios
 * es solo para ADMIN mientras que el resto de /api/** es para USER o ADMIN(trabajo de recepción).
 * La API es stateless, no se guarda sesión en el servidor, cada petición protegida manda su propio JWT.
 *
 * <p>Los rechazos de seguridad(401 y 403) pasan en los filtros antes de llegar a un controller,
 * por eso se mandan al manejador global para que respondan con el mismo formato ApiError que el
 * resto de la API, la decisión de permitir o negar sigue estando en esta configuración.
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final HandlerExceptionResolver exceptionResolver;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.exceptionResolver = exceptionResolver;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF no aplica porque la API no usa cookies ni sesión y se autentica con Bearer token.
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
                        // Página de error de Spring, se deja pública para ver el estado real(400, 404).
                        .requestMatchers("/error").permitAll()
                        // Rutas de Swagger y OpenAPI, solo muestran documentación y no datos del negocio.
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/veterinarios", "/api/veterinarios/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        // 401 cuando no hay token o el token es inválido o está vencido
                        .authenticationEntryPoint((request, response, error) ->
                                exceptionResolver.resolveException(request, response, null, error))
                        // 403 cuando sí inició sesión pero no tiene el rol que se pide
                        .accessDeniedHandler((request, response, error) ->
                                exceptionResolver.resolveException(request, response, null, error)))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
