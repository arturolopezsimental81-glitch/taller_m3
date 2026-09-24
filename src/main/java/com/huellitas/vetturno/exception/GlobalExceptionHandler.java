package com.huellitas.vetturno.exception;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Convierte las excepciones de toda la API en respuestas ApiError, nunca se mandan al cliente
 * clases, trazas ni mensajes internos y el detalle técnico solo queda en el log del servidor.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** 400 cuando no se cumplen las reglas de Bean Validation de los DTO de entrada(@Valid). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new TreeMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.merge(error.getField(), error.getDefaultMessage(), (a, b) -> a + "; " + b);
        }
        return responder(HttpStatus.BAD_REQUEST, "Los datos enviados no son válidos", errores);
    }

    /** 400 cuando no se cumple una regla del negocio(referencia que no existe, fecha pasada u horario ocupado). */
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ApiError> manejarReglaNegocio(ReglaNegocioException ex) {
        return responder(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    /** 400 cuando el JSON está mal escrito o trae un tipo incorrecto(por ejemplo una fecha inválida). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        return responder(HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición no es un JSON válido. Revisa los tipos; "
                        + "las fechas usan el formato yyyy-MM-ddTHH:mm",
                null);
    }

    /** 400 cuando un parámetro de la ruta trae un tipo incorrecto(por ejemplo /veterinario/abc). */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> manejarTipoParametro(MethodArgumentTypeMismatchException ex) {
        return responder(HttpStatus.BAD_REQUEST,
                "El parámetro '" + ex.getName() + "' tiene un valor inválido", null);
    }

    /**
     * 400 cuando la base rechaza la operación por una restricción, por ejemplo dos peticiones
     * al mismo tiempo para el mismo veterinario y horario.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> manejarIntegridad(DataIntegrityViolationException ex) {
        log.warn("Restricción de datos violada: {}", ex.getMostSpecificCause().getMessage());
        return responder(HttpStatus.BAD_REQUEST,
                "La operación entra en conflicto con datos existentes (por ejemplo, un horario ya ocupado)",
                null);
    }

    /** 401 cuando el email o la contraseña del login son incorrectos. */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> manejarCredenciales(BadCredentialsException ex) {
        return responder(HttpStatus.UNAUTHORIZED, "Email o contraseña incorrectos", null);
    }

    /** 401 cuando una ruta protegida llega sin token o con un token inválido o vencido(viene de SecurityConfig). */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> manejarNoAutenticado(AuthenticationException ex) {
        return responder(HttpStatus.UNAUTHORIZED,
                "Debes iniciar sesión: envía un token válido en el encabezado Authorization: Bearer", null);
    }

    /** 403 cuando el usuario sí inició sesión pero no tiene el rol que se pide(viene de SecurityConfig). */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> manejarSinPermiso(AccessDeniedException ex) {
        return responder(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esta operación", null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> manejarRutaInexistente(NoResourceFoundException ex) {
        return responder(HttpStatus.NOT_FOUND, "La ruta solicitada no existe", null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> manejarMetodo(HttpRequestMethodNotSupportedException ex) {
        return responder(HttpStatus.METHOD_NOT_ALLOWED,
                "El método " + ex.getMethod() + " no está permitido en esta ruta", null);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> manejarMediaType(HttpMediaTypeNotSupportedException ex) {
        return responder(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "El cuerpo debe enviarse como application/json", null);
    }

    /** 500 para cualquier falla que no se esperaba, se responde un mensaje genérico y el detalle se va al log. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarGeneral(Exception ex) {
        log.error("Error inesperado", ex);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado en el servidor", null);
    }

    private ResponseEntity<ApiError> responder(HttpStatus status, String mensaje, Map<String, String> errores) {
        return ResponseEntity.status(status).body(new ApiError(status.value(), mensaje, errores));
    }
}
