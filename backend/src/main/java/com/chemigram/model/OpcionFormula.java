
package com.chemigram.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "opciones_formula")
public class OpcionFormula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contenido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaOpcion categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formula_id", nullable = false)
    private Formula formula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proponente_id", nullable = false)
    private Usuario proponente;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    public OpcionFormula() {
    }

    public OpcionFormula(
            String contenido,
            CategoriaOpcion categoria,
            Formula formula,
            Usuario proponente) {

        this.contenido = contenido;
        this.categoria = categoria;
        this.formula = formula;
        this.proponente = proponente;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getContenido() {
        return contenido;
    }

    public CategoriaOpcion getCategoria() {
        return categoria;
    }

    public Formula getFormula() {
        return formula;
    }

    public Usuario getProponente() {
        return proponente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}

