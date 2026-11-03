
package com.chemigram.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "votos",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                "formula_id",
                "categoria",
                "usuario_id"
            }
        )
    }
)
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formula_id", nullable = false)
    private Formula formula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opcion_id", nullable = false)
    private OpcionFormula opcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaOpcion categoria;

    @Column(nullable = false)
    private LocalDateTime fechaVoto;

    public Voto() {
    }

    public Voto(
            Formula formula,
            OpcionFormula opcion,
            Usuario usuario,
            CategoriaOpcion categoria) {

        this.formula = formula;
        this.opcion = opcion;
        this.usuario = usuario;
        this.categoria = categoria;
        this.fechaVoto = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Formula getFormula() {
        return formula;
    }

    public OpcionFormula getOpcion() {
        return opcion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public CategoriaOpcion getCategoria() {
        return categoria;
    }

    public LocalDateTime getFechaVoto() {
        return fechaVoto;
    }

    public void setOpcion(OpcionFormula opcion) {
        this.opcion = opcion;
        this.fechaVoto = LocalDateTime.now();
    }
}
