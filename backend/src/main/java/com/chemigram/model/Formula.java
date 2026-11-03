package com.chemigram.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "formulas")
public class Formula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creador_id", nullable = false)
    private Usuario creador;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gremio_id", nullable = false)
    private Gremio gremio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFormula estado = EstadoFormula.ABIERTA;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public Formula() {}

    public Formula(
            String nombre,
            String descripcion,
            Usuario creador,
            Gremio gremio) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creador = creador;
        this.gremio = gremio;
        this.estado = EstadoFormula.ABIERTA;
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
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getCreador() { return creador; }
    public Gremio getGremio() { return gremio; }

    public EstadoFormula getEstado() { return estado; }

    public void setEstado(EstadoFormula estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}
