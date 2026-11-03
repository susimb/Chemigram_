package com.chemigram.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private String especialidad;

    private int puntosAlquimia = 0;

    private int rarezaTotal = 0;

    // Constructor vacío requerido por JPA
    public Usuario() {
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public int getPuntosAlquimia() {
        return puntosAlquimia;
    }

    public void setPuntosAlquimia(int puntosAlquimia) {
        this.puntosAlquimia = puntosAlquimia;
    }

    public int getRarezaTotal() {
        return rarezaTotal;
    }

    public void setRarezaTotal(int rarezaTotal) {
        this.rarezaTotal = rarezaTotal;
    }
}
