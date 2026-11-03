
package com.chemigram.controller;

import com.chemigram.model.Pocion;
import com.chemigram.service.FinalizarFormulaService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/formulas")
public class FinalizarFormulaController {

    private final FinalizarFormulaService finalizarService;

    public FinalizarFormulaController(
            FinalizarFormulaService finalizarService) {
        this.finalizarService = finalizarService;
    }

    @PostMapping("/{formulaId}/finalizar")
    public Pocion finalizarFormula(
            @PathVariable Long formulaId,
            @RequestParam String usuario) {

        return finalizarService.finalizarFormula(formulaId, usuario);
    }
}
