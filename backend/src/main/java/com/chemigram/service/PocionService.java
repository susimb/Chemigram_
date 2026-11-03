package com.chemigram.service;

import com.chemigram.model.Pocion;
import com.chemigram.model.Usuario;
import com.chemigram.repository.PocionRepository;
import com.chemigram.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PocionService {

    private final PocionRepository pocionRepository;
    private final UsuarioRepository usuarioRepository;

    public PocionService(
            PocionRepository pocionRepository,
            UsuarioRepository usuarioRepository) {
        this.pocionRepository = pocionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Pocion crearPocion(
        String nombre,
        String descripcion,
        int rareza,
        Long usuarioId) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la poción es obligatorio."
            );
        }

        if (rareza < 0) {
            throw new IllegalArgumentException(
                    "La rareza no puede ser negativa."
            );
        }

        Usuario creador = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario no existe."
                ));

        Pocion pocion = new Pocion(
        nombre,
        descripcion,
        rareza,
        creador
);


        Pocion pocionGuardada = pocionRepository.save(pocion);

        creador.setRarezaTotal(
                creador.getRarezaTotal() + rareza
        );

        usuarioRepository.save(creador);

        return pocionGuardada;
    }
}
