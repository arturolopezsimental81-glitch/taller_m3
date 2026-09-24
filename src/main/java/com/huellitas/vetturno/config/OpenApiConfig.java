package com.huellitas.vetturno.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Datos de la API en OpenAPI y esquema Bearer JWT, el botón Authorize de Swagger UI usa
 * bearerAuth para mandar el token en las peticiones protegidas.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "VetTurno API",
                version = "1.0",
                description = "Agenda digital de Veterinaria Huellitas donde se registran responsables, "
                        + "mascotas y veterinarios y se agendan citas sin cruces de horario. Para probarla "
                        + "se registra o se inicia sesión en /api/auth, se copia el token y se pega en "
                        + "Authorize, recepción(USER) agenda citas y solo ADMIN registra veterinarios."),
        security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
public class OpenApiConfig {
}
