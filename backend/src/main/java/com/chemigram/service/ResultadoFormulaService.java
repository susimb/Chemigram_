
package com.chemigram.service;

import com.chemigram.model.*;
import com.chemigram.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResultadoFormulaService {

    private final FormulaRepository formulaRepository;
    private final VotoRepository votoRepository;
    private final VotoService votoService;

    public ResultadoFormulaService(
            FormulaRepository formulaRepository,
            VotoRepository votoRepository,
            VotoService votoService) {

        this.formulaRepository = formulaRepository;
        this.votoRepository = votoRepository;
        this.votoService = votoService;
    }

    @Transactional(readOnly = true)
    public List<ResultadoCategoria> calcularResultados(Long formulaId) {

        formulaRepository.findById(formulaId)
                .orElseThrow(() ->
                        new IllegalArgumentException("La fórmula no existe.")
                );

        List<Voto> votos = votoRepository.findByFormulaId(formulaId);

        List<ResultadoCategoria> resultados = new ArrayList<>();

        for (CategoriaOpcion categoria : CategoriaOpcion.values()) {

            List<Voto> votosCategoria = votos.stream()
                    .filter(voto -> voto.getCategoria() == categoria)
                    .toList();

            if (votosCategoria.isEmpty()) {
                continue;
            }

            Map<Long, Integer> puntuaciones = new HashMap<>();

            Map<Long, OpcionFormula> opciones = new HashMap<>();

            for (Voto voto : votosCategoria) {

                Long opcionId = voto.getOpcion().getId();

                int peso = votoService.calcularPesoVoto(voto);

                puntuaciones.merge(opcionId, peso, Integer::sum);

                opciones.put(opcionId, voto.getOpcion());
            }

            int puntuacionMaxima = Collections.max(
                    puntuaciones.values()
            );

            List<OpcionFormula> opcionesGanadoras = puntuaciones.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() == puntuacionMaxima)
                    .map(entry -> opciones.get(entry.getKey()))
                    .collect(Collectors.toList());

            boolean empate = opcionesGanadoras.size() > 1;

            ResultadoCategoria resultado = new ResultadoCategoria(
                    categoria,
                    empate ? null : opcionesGanadoras.get(0),
                    empate ? opcionesGanadoras : List.of(),
                    puntuacionMaxima,
                    empate
            );

            resultados.add(resultado);
        }

        return resultados;
    }
}
