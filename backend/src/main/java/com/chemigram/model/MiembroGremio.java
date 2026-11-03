
package com.chemigram.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "miembros_gremio",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"usuario_id", "gremio_id"}
        )
    }
)
public class MiembroGremio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gremio_id", nullable = false)
    private Gremio gremio;

    @Column(nullable = false)
    private boolean esCatador = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaIngreso;

    public MiembroGremio() {}

    public MiembroGremio(Usuario usuario, Gremio gremio) {
        this.usuario = usuario;
        this.gremio = gremio;
        this.esCatador = false;
        this.fechaIngreso = LocalDateTime.now();
    }

    @PrePersist
    protected void antesDeGuardar() {
        if (fechaIngreso == null) {
            fechaIngreso = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Gremio getGremio() { return gremio; }
    public void setGremio(Gremio gremio) {
        this.gremio = gremio;
    }

    public boolean isEsCatador() { return esCatador; }

    public void convertirEnCatador() {
        this.esCatador = true;
    }

    public void quitarRolCatador() {
        this.esCatador = false;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }
}
