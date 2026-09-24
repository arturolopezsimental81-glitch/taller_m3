package com.huellitas.vetturno.exception;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Formato de los errores de VetTurno, todos los endpoints responden sus errores con esta
 * misma forma.
 */
public class ApiError {

    private final int status;
    private final String mensaje;
    private final Map<String, String> errores;
    private final String timestamp;

    public ApiError(int status, String mensaje, Map<String, String> errores) {
        this.status = status;
        this.mensaje = mensaje;
        this.errores = errores == null
                ? Map.of()
                : Collections.unmodifiableMap(new LinkedHashMap<>(errores));
        this.timestamp = LocalDateTime.now().toString();
    }

    public int getStatus() {
        return status;
    }

    public String getMensaje() {
        return mensaje;
    }

    /** Errores por campo(nombre del campo y su mensaje), queda vacío cuando el error no es de un campo. */
    public Map<String, String> getErrores() {
        return errores;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
