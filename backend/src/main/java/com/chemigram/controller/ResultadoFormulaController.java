package com.chemigram.controller;

import com.chemigram.model.ResultadoCategoria;
import com.chemigram.service.ResultadoFormulaService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resultados")
public class ResultadoFormulaController {

    private final ResultadoFormulaService resultadoService;

    public ResultadoFormulaController(
            ResultadoFormulaService resultadoService) {
        this.resultadoService = resultadoService;
    }

    @GetMapping("/formula/{formulaId}")
    public List<ResultadoCategoria> calcularResultados(
            @PathVariable Long formulaId) {

        return resultadoService.calcularResultados(formulaId);
    }
}
