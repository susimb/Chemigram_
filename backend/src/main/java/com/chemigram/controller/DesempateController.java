package com.chemigram.controller;

import com.chemigram.model.Desempate;
import com.chemigram.service.DesempateService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/desempates")
public class DesempateController {

    private final DesempateService desempateService;

    public DesempateController(DesempateService desempateService) {
        this.desempateService = desempateService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Desempate resolverEmpate(
            @RequestParam Long formulaId,
            @RequestParam Long opcionId,
            Authentication authentication) {

        String email = authentication.getName();

        return desempateService.resolverEmpate(
                formulaId,
                email,
                opcionId
        );
    }
}

