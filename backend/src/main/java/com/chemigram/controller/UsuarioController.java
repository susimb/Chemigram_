
package com.chemigram.controller;

import com.chemigram.dto.RegistroUsuarioRequest;
import com.chemigram.dto.UsuarioResponse;
import com.chemigram.dto.LoginRequest;
import com.chemigram.dto.LoginResponse;
import com.chemigram.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrarUsuario(
            @Valid @RequestBody RegistroUsuarioRequest request) {

        UsuarioResponse usuario =
                usuarioService.registrarUsuario(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> iniciarSesion(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse respuesta =
                usuarioService.iniciarSesion(request);

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioResponse> obtenerPerfil(
            Authentication authentication) {

        UsuarioResponse usuario =
                usuarioService.buscarPorEmail(
                        authentication.getName());

        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/privado")
    public ResponseEntity<String> rutaPrivada() {
        return ResponseEntity.ok(
                "¡Acceso autorizado a Chemigram!");
    }
}
