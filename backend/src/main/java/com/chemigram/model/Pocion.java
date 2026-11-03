
package com.chemigram.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
@Table(name = "pociones")
public class Pocion {
    
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "formula_id", unique = true)
private Formula formula;

public Formula getFormula() {
    return formula;
}

public void setFormula(Formula formula) {
    this.formula = formula;
}

    @Column(length = 1000)
    private String descripcion;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private int rareza;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creador_id", nullable = false)
    private Usuario creador;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // Constructor requerido por JPA
    public Pocion() {
    }

    // Inicializa los datos de una poción nueva
    public Pocion(
        String nombre,
        String descripcion,
        int rareza,
        Usuario creador) {

    this.nombre = nombre;
    this.descripcion = descripcion;
    this.rareza = rareza;
    this.creador = creador;
    this.fechaCreacion = LocalDateTime.now();
}

    @PrePersist
    protected void antesDeGuardar() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
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

    public int getRareza() {
        return rareza;
    }

    public void setRareza(int rareza) {
        this.rareza = rareza;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public String getDescripcion() {
    return descripcion;
    }
}
