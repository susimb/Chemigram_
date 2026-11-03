
package com.chemigram.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "desempates",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"formula_id", "categoria"}
        )
    }
)
public class Desempate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formula_id", nullable = false)
    private Formula formula;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaOpcion categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opcion_ganadora_id", nullable = false)
    private OpcionFormula opcionGanadora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catador_id", nullable = false)
    private Usuario catador;

    @Column(nullable = false)
    private LocalDateTime fechaDecision;

    public Desempate() {
    }

    public Desempate(
            Formula formula,
            CategoriaOpcion categoria,
            OpcionFormula opcionGanadora,
            Usuario catador) {

        this.formula = formula;
        this.categoria = categoria;
        this.opcionGanadora = opcionGanadora;
        this.catador = catador;
        this.fechaDecision = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Formula getFormula() {
        return formula;
    }

    public CategoriaOpcion getCategoria() {
        return categoria;
    }

    public OpcionFormula getOpcionGanadora() {
        return opcionGanadora;
    }

    public Usuario getCatador() {
        return catador;
    }

    public LocalDateTime getFechaDecision() {
        return fechaDecision;
    }
}
