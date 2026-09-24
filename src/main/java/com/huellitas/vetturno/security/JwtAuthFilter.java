package com.huellitas.vetturno.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Lee el encabezado Authorization: Bearer de cada petición para poner al usuario en el
 * SecurityContext siempre y cuando el token sea válido, si no hay token o no es válido la
 * petición sigue sin usuario, después SecurityConfig decide si la rechaza.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String PREFIJO_BEARER = "Bearer ";

    private final JwtService jwtService;
    private final UsuarioDetailsService usuarioDetailsService;

    public JwtAuthFilter(JwtService jwtService, UsuarioDetailsService usuarioDetailsService) {
        this.jwtService = jwtService;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String encabezado = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (encabezado == null || !encabezado.startsWith(PREFIJO_BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = encabezado.substring(PREFIJO_BEARER.length());
        if (jwtService.esValido(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            autenticar(request, jwtService.extraerEmail(token));
        }
        filterChain.doFilter(request, response);
    }

    private void autenticar(HttpServletRequest request, String email) {
        try {
            // Se carga el usuario desde MySQL para que el rol sea siempre el que está guardado en la base.
            UserDetails usuario = usuarioDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken autenticacion = new UsernamePasswordAuthenticationToken(
                    usuario, null, usuario.getAuthorities());
            autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(autenticacion);
        } catch (UsernameNotFoundException e) {
            // Si el token es de un usuario que ya no existe la petición sigue como anónima.
            SecurityContextHolder.clearContext();
        }
    }
}
