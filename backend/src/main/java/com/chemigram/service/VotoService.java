
package com.chemigram.service;

import com.chemigram.model.*;
import com.chemigram.repository.*;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final FormulaRepository formulaRepository;
    private final OpcionFormulaRepository opcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final MiembroGremioRepository miembroRepository;
    private final NotificacionService notificacionService;

    public VotoService(
            VotoRepository votoRepository,
            FormulaRepository formulaRepository,
            OpcionFormulaRepository opcionRepository,
            UsuarioRepository usuarioRepository,
            MiembroGremioRepository miembroRepository,
            NotificacionService notificacionService) {

        this.votoRepository = votoRepository;
        this.formulaRepository = formulaRepository;
        this.opcionRepository = opcionRepository;
        this.usuarioRepository = usuarioRepository;
        this.miembroRepository = miembroRepository;
        this.notificacionService = notificacionService;
    }

    @Transactional
    public Voto registrarVoto(
            Long formulaId,
            Long opcionId,
            String email) {

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "El usuario autenticado no existe."
            );
        }

        Formula formula = formulaRepository.findById(formulaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La fórmula no existe."
                        )
                );

        if (formula.getEstado() != EstadoFormula.EN_VOTACION) {
            throw new IllegalStateException(
                    "Esta fórmula no está en periodo de votación."
            );
        }

        Long usuarioId = usuario.getId();
        Long gremioId = formula.getGremio().getId();

        boolean esMiembro = miembroRepository
                .existsByUsuarioIdAndGremioId(usuarioId, gremioId);

        if (!esMiembro) {
            throw new AccessDeniedException(
                    "Debes pertenecer al gremio para votar."
            );
        }

        OpcionFormula opcion = opcionRepository.findById(opcionId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La opción no existe."
                        )
                );

        if (!opcion.getFormula().getId().equals(formulaId)) {
            throw new IllegalArgumentException(
                    "La opción no pertenece a esta fórmula."
            );
        }

        CategoriaOpcion categoria = opcion.getCategoria();

        Voto votoExistente = votoRepository
                .findByFormulaIdAndCategoriaAndUsuarioId(
                        formulaId,
                        categoria,
                        usuarioId
                )
                .orElse(null);

        Voto votoGuardado;

        if (votoExistente != null) {

            votoExistente.setOpcion(opcion);
            votoGuardado = votoRepository.save(votoExistente);

        } else {

            Voto nuevoVoto = new Voto(
                    formula,
                    opcion,
                    usuario,
                    categoria
            );

            votoGuardado = votoRepository.save(nuevoVoto);

            // Recompensa únicamente por el primer voto.
            usuario.setPuntosAlquimia(
                    usuario.getPuntosAlquimia() + 1
            );

            usuarioRepository.save(usuario);
        }

        // Notificar a los integrantes del gremio.
        Map<String, Object> mensaje = Map.of(
                "tipo", "VOTO_ACTUALIZADO",
                "formulaId", formulaId,
                "categoria", categoria.name(),
                "usuario", usuario.getNombre()
        );

        notificacionService.notificarGremio(gremioId, mensaje);

        return votoGuardado;
    }

    @Transactional(readOnly = true)
    public List<Voto> listarVotosPorFormula(
            Long formulaId,
            String email) {

        Formula formula = obtenerFormulaDeMiembro(formulaId, email);

        return votoRepository.findByFormulaId(formula.getId());
    }

    @Transactional(readOnly = true)
    public List<Voto> listarVotosPorCategoria(
            Long formulaId,
            CategoriaOpcion categoria,
            String email) {

        Formula formula = obtenerFormulaDeMiembro(formulaId, email);

        return votoRepository.findByFormulaIdAndCategoria(
                formula.getId(),
                categoria
        );
    }

    private Formula obtenerFormulaDeMiembro(
            Long formulaId,
            String email) {

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "El usuario autenticado no existe."
            );
        }

        Formula formula = formulaRepository.findById(formulaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La fórmula no existe."
                        )
                );

        Long usuarioId = usuario.getId();
        Long gremioId = formula.getGremio().getId();

        boolean esMiembro = miembroRepository
                .existsByUsuarioIdAndGremioId(usuarioId, gremioId);

        if (!esMiembro) {
            throw new AccessDeniedException(
                    "No puedes consultar los votos de un gremio al que no perteneces."
            );
        }

        return formula;
    }

    @Transactional(readOnly = true)
    public int calcularPesoVoto(Voto voto) {

        Long gremioId = voto.getFormula().getGremio().getId();

        return miembroRepository
                .findByUsuarioIdAndGremioId(
                        voto.getUsuario().getId(),
                        gremioId
                )
                .map(miembro -> miembro.isEsCatador() ? 2 : 1)
                .orElse(0);
    }
}




