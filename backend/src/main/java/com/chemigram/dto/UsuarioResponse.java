package com.chemigram.dto;

public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String email;
    private String especialidad;
    private int puntosAlquimia;
    private int rarezaTotal;

    public UsuarioResponse(Long id, String nombre, String email,
            String especialidad, int puntosAlquimia, int rarezaTotal) {

        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.especialidad = especialidad;
        this.puntosAlquimia = puntosAlquimia;
        this.rarezaTotal = rarezaTotal;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public int getPuntosAlquimia() {
        return puntosAlquimia;
    }

    public int getRarezaTotal() {
        return rarezaTotal;
    }
}