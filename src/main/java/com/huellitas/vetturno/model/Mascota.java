package com.huellitas.vetturno.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Paciente de la veterinaria que pertenece a un solo propietario, la llave foránea
 * propietario_id se guarda en esta tabla porque es el lado muchos de la relación.
 */
@Entity
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(nullable = false, length = 40)
    private String especie;

    @Column(length = 60)
    private String raza;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "propietario_id", nullable = false)
    private Propietario propietario;

    public Mascota() {
    }

    public Mascota(String nombre, String especie, String raza, Propietario propietario) {
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.propietario = propietario;
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

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }
}
