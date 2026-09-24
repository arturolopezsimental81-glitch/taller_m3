package com.huellitas.vetturno.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Genera, firma y valida los JWT de VetTurno, el token va firmado con HMAC-SHA pero no va
 * cifrado, cualquiera puede leer su contenido. Por eso solo lleva el email con el rol, nunca
 * la contraseña, la clave y la vigencia se leen de la configuración, no del código.
 */
@Service
public class JwtService {

    /** HMAC-SHA pide una clave de al menos 256 bits(32 bytes). */
    private static final int LONGITUD_MINIMA_CLAVE = 32;

    private final SecretKey clave;
    private final long expiracionMs;

    public JwtService(@Value("${jwt.secret}") String secreto,
                      @Value("${jwt.expiration-ms}") long expiracionMs) {
        byte[] bytes = secreto.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < LONGITUD_MINIMA_CLAVE) {
            throw new IllegalStateException(
                    "jwt.secret debe tener al menos " + LONGITUD_MINIMA_CLAVE + " caracteres");
        }
        this.clave = Keys.hmacShaKeyFor(bytes);
        this.expiracionMs = expiracionMs;
    }

    public String generarToken(String email, String rol) {
        Date ahora = new Date();
        Date expira = new Date(ahora.getTime() + expiracionMs);
        return Jwts.builder()
                .subject(email)
                .claim("rol", rol)
                .issuedAt(ahora)
                .expiration(expira)
                .signWith(clave)
                .compact();
    }

    public String extraerEmail(String token) {
        return parsearClaims(token).getSubject();
    }

    /** Revisa la firma y la vigencia, un token modificado o vencido no es válido. */
    public boolean esValido(String token) {
        try {
            parsearClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parsearClaims(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
