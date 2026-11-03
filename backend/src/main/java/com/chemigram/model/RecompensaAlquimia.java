
package com.chemigram.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "recompensas_alquimia",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_recompensa_opcion",
            columnNames = {"opcion_id"}
        )
    }
)
public class RecompensaAlquimia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formula_id", nullable = false)
    private Formula formula;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opcion_id", nullable = false)
    private OpcionFormula opcion;

    @Column(nullable = false)
    private int puntos;

    @Column(nullable = false)
    private LocalDateTime fechaAsignacion;

    public RecompensaAlquimia() {
    }

    public RecompensaAlquimia(
            Usuario usuario,
            Formula formula,
            OpcionFormula opcion,
            int puntos) {

        this.usuario = usuario;
        this.formula = formula;
        this.opcion = opcion;
        this.puntos = puntos;
        this.fechaAsignacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Formula getFormula() {
        return formula;
    }

    public OpcionFormula getOpcion() {
        return opcion;
    }

    public int getPuntos() {
        return puntos;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }
}

