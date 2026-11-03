
package com.chemigram.controller;

import com.chemigram.model.Pocion;
import com.chemigram.service.PocionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pociones")
public class PocionController {

    private final PocionService pocionService;

    public PocionController(PocionService pocionService) {
        this.pocionService = pocionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pocion crearPocion(
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam int rareza,
            @RequestParam Long usuarioId) {
        return pocionService.crearPocion(nombre, descripcion, rareza, usuarioId);
    }
}
