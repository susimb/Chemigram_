package com.chemigram.service;

import com.chemigram.dto.UsuarioResponse;
import com.chemigram.model.Usuario;
import com.chemigram.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RankingService {

    private final UsuarioRepository usuarioRepository;

    public RankingService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioResponse> obtenerRanking() {

        return usuarioRepository.findAll()
                .stream()
                .sorted(
                    Comparator
                        .comparingInt(Usuario::getPuntosAlquimia)
                        .reversed()
                        .thenComparing(
                            Comparator.comparingInt(
                                Usuario::getRarezaTotal
                            ).reversed()
                        )
                )
                .map(usuario -> new UsuarioResponse(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getEmail(),
                    usuario.getEspecialidad(),
                    usuario.getPuntosAlquimia(),
                    usuario.getRarezaTotal()
                ))
                .toList();
    }
}
