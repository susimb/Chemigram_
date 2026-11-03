
package com.chemigram.controller;

import com.chemigram.model.Gremio;
import com.chemigram.model.MiembroGremio;
import com.chemigram.service.GremioService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gremios")
public class GremioController {

    private final GremioService gremioService;

    public GremioController(GremioService gremioService) {
        this.gremioService = gremioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Gremio crearGremio(
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) String icono,
            Authentication authentication) {

        return gremioService.crearGremio(
                nombre,
                descripcion,
                icono,
                authentication.getName()
        );
    }

    @GetMapping
    public List<Gremio> listarGremios() {
        return gremioService.listarGremios();
    }

    @GetMapping("/{gremioId}/miembros")
    public List<MiembroGremio> listarMiembros(
            @PathVariable Long gremioId,
            Authentication authentication) {

        return gremioService.listarMiembros(
                gremioId,
                authentication.getName()
        );
    }

    @PostMapping("/{gremioId}/miembros")
    @ResponseStatus(HttpStatus.CREATED)
    public MiembroGremio unirUsuario(
            @PathVariable Long gremioId,
            Authentication authentication) {

        return gremioService.unirUsuario(
                gremioId,
                authentication.getName()
        );
    }

    @PutMapping("/{gremioId}/catador/{usuarioId}")
    public MiembroGremio asignarCatador(
            @PathVariable Long gremioId,
            @PathVariable Long usuarioId,
            Authentication authentication) {

        return gremioService.asignarCatador(
                gremioId,
                usuarioId,
                authentication.getName()
        );
    }

    @DeleteMapping("/{gremioId}/catador")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void retirarCatador(
            @PathVariable Long gremioId,
            Authentication authentication) {

        gremioService.retirarCatador(
                gremioId,
                authentication.getName()
        );
    }
}
