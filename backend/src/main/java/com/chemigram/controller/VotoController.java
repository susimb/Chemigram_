
package com.chemigram.controller;

import com.chemigram.model.Voto;
import com.chemigram.model.CategoriaOpcion;
import com.chemigram.service.VotoService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votos")
public class VotoController {

    private final VotoService votoService;

    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Voto registrarVoto(
            @RequestParam Long formulaId,
            @RequestParam Long opcionId,
            Authentication authentication) {

        String email = authentication.getName();

        return votoService.registrarVoto(
                formulaId,
                opcionId,
                email
        );
    }

    @GetMapping("/formula/{formulaId}")
    public List<Voto> listarPorFormula(
            @PathVariable Long formulaId,
            Authentication authentication) {

        String email = authentication.getName();

        return votoService.listarVotosPorFormula(
                formulaId,
                email
        );
    }

    @GetMapping("/formula/{formulaId}/categoria/{categoria}")
    public List<Voto> listarPorCategoria(
            @PathVariable Long formulaId,
            @PathVariable CategoriaOpcion categoria,
            Authentication authentication) {

        String email = authentication.getName();

        return votoService.listarVotosPorCategoria(
                formulaId,
                categoria,
                email
        );
    }
}



