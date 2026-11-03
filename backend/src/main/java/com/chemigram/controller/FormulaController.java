package com.chemigram.controller;

import com.chemigram.model.EstadoFormula;
import com.chemigram.model.Formula;
import com.chemigram.service.FormulaService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formulas")
public class FormulaController {

    private final FormulaService formulaService;

    public FormulaController(FormulaService formulaService) {
        this.formulaService = formulaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Formula crearFormula(
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam Long gremioId,
            Authentication authentication) {

        return formulaService.crearFormula(
                nombre,
                descripcion,
                gremioId,
                authentication.getName()
        );
    }

    @GetMapping("/gremio/{gremioId}")
    public List<Formula> listarPorGremio(
            @PathVariable Long gremioId,
            Authentication authentication) {

        return formulaService.listarPorGremio(
                gremioId,
                authentication.getName()
        );
    }

    @PutMapping("/{formulaId}/estado")
    public Formula cambiarEstado(
            @PathVariable Long formulaId,
            @RequestParam EstadoFormula nuevoEstado,
            Authentication authentication) {

        return formulaService.cambiarEstado(
                formulaId,
                nuevoEstado,
                authentication.getName()
        );
    }
}


