
package com.chemigram.controller;

import com.chemigram.model.CategoriaOpcion;
import com.chemigram.model.OpcionFormula;
import com.chemigram.service.OpcionFormulaService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opciones")
public class OpcionFormulaController {

    private final OpcionFormulaService opcionService;

    public OpcionFormulaController(
            OpcionFormulaService opcionService) {
        this.opcionService = opcionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OpcionFormula crearOpcion(
            @RequestParam String contenido,
            @RequestParam CategoriaOpcion categoria,
            @RequestParam Long formulaId,
            Authentication authentication) {

        return opcionService.crearOpcion(
                contenido,
                categoria,
                formulaId,
                authentication.getName()
        );
    }

    @GetMapping("/formula/{formulaId}")
    public List<OpcionFormula> listarPorFormula(
            @PathVariable Long formulaId,
            Authentication authentication) {

        return opcionService.listarPorFormula(
                formulaId,
                authentication.getName()
        );
    }

    @GetMapping("/formula/{formulaId}/categoria/{categoria}")
    public List<OpcionFormula> listarPorCategoria(
            @PathVariable Long formulaId,
            @PathVariable CategoriaOpcion categoria,
            Authentication authentication) {

        return opcionService.listarPorCategoria(
                formulaId,
                categoria,
                authentication.getName()
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<OpcionFormula> listarPorUsuario(
            @PathVariable Long usuarioId,
            Authentication authentication) {

        return opcionService.listarPorUsuario(
                usuarioId,
                authentication.getName()
        );
    }
}
