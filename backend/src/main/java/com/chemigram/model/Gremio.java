
package com.chemigram.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "gremios")
public class Gremio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private String icono = "⚗️";

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creador_id", nullable = false)
    private Usuario creador;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public Gremio() {}

    public Gremio(String nombre, String descripcion,
                  String icono, Usuario creador) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.icono = icono;
        this.creador = creador;
        this.fechaCreacion = LocalDateTime.now();
    }

    @PrePersist
    protected void antesDeGuardar() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }

    public Usuario getCreador() { return creador; }
    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}
