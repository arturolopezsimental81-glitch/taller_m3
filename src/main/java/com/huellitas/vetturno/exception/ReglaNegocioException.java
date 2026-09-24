package com.huellitas.vetturno.exception;

/**
 * Se lanza cuando una operación no cumple una regla de la veterinaria(una referencia que no
 * existe, una fecha pasada o un horario ocupado), el mensaje se puede mostrar al cliente.
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
