package com.chemigram.model;

import java.util.List;

public class ResultadoCategoria {

    private CategoriaOpcion categoria;
    private OpcionFormula opcionGanadora;
    private List<OpcionFormula> opcionesEmpatadas;
    private int votosPonderados;
    private boolean requiereDesempate;

    public ResultadoCategoria(
            CategoriaOpcion categoria,
            OpcionFormula opcionGanadora,
            List<OpcionFormula> opcionesEmpatadas,
            int votosPonderados,
            boolean requiereDesempate) {

        this.categoria = categoria;
        this.opcionGanadora = opcionGanadora;
        this.opcionesEmpatadas = opcionesEmpatadas;
        this.votosPonderados = votosPonderados;
        this.requiereDesempate = requiereDesempate;
    }

    public CategoriaOpcion getCategoria() {
        return categoria;
    }

    public OpcionFormula getOpcionGanadora() {
        return opcionGanadora;
    }

    public List<OpcionFormula> getOpcionesEmpatadas() {
        return opcionesEmpatadas;
    }

    public int getVotosPonderados() {
        return votosPonderados;
    }

    public boolean isRequiereDesempate() {
        return requiereDesempate;
    }
}

