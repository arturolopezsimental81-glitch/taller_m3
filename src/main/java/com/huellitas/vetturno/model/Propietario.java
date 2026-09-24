package com.huellitas.vetturno.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Persona responsable de una o varias mascotas, no se navega del propietario hacia sus
 * mascotas porque la relación se guarda en Mascota(lado muchos) y ningún endpoint necesita
 * recorrer esa lista, así se evita la recursión JSON y la carga de colecciones que no se usan.
 */
@Entity
public class Propietario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(length = 120)
    private String email;

    public Propietario() {
    }

    public Propietario(String nombre, String telefono, String email) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
