package com.chemigram.service;

import com.chemigram.model.EstadoFormula;
import com.chemigram.model.Formula;
import com.chemigram.model.Gremio;
import com.chemigram.model.Usuario;

import com.chemigram.repository.FormulaRepository;
import com.chemigram.repository.GremioRepository;
import com.chemigram.repository.MiembroGremioRepository;
import com.chemigram.repository.UsuarioRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FormulaService {

    private final FormulaRepository formulaRepository;
    private final GremioRepository gremioRepository;
    private final UsuarioRepository usuarioRepository;
    private final MiembroGremioRepository miembroRepository;

    public FormulaService(
            FormulaRepository formulaRepository,
            GremioRepository gremioRepository,
            UsuarioRepository usuarioRepository,
            MiembroGremioRepository miembroRepository) {

        this.formulaRepository = formulaRepository;
        this.gremioRepository = gremioRepository;
        this.usuarioRepository = usuarioRepository;
        this.miembroRepository = miembroRepository;
    }

    @Transactional
    public Formula crearFormula(
            String nombre,
            String descripcion,
            Long gremioId,
            String email) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la fórmula es obligatorio."
            );
        }

        Usuario creador = obtenerUsuario(email);

        Gremio gremio = gremioRepository.findById(gremioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El gremio no existe."
                ));

        boolean esMiembro = miembroRepository
                .existsByUsuarioIdAndGremioId(
                        creador.getId(),
                        gremioId
                );

        if (!esMiembro) {
            throw new AccessDeniedException(
                    "Debes pertenecer al gremio para crear fórmulas."
            );
        }

        String nombreNormalizado = nombre.trim();

        if (formulaRepository.existsByNombreIgnoreCaseAndGremioId(
                nombreNormalizado, gremioId)) {
            throw new IllegalArgumentException(
                    "Ya existe una fórmula con ese nombre en este gremio."
            );
        }

        Formula formula = new Formula(
                nombreNormalizado,
                descripcion,
                creador,
                gremio
        );

        return formulaRepository.save(formula);
    }

    @Transactional(readOnly = true)
    public List<Formula> listarPorGremio(
            Long gremioId,
            String email) {

        Usuario usuario = obtenerUsuario(email);

        if (!gremioRepository.existsById(gremioId)) {
            throw new IllegalArgumentException(
                    "El gremio no existe."
            );
        }

        boolean esMiembro = miembroRepository
                .existsByUsuarioIdAndGremioId(
                        usuario.getId(),
                        gremioId
                );

        if (!esMiembro) {
            throw new AccessDeniedException(
                    "Debes pertenecer al gremio para consultar sus fórmulas."
            );
        }

        return formulaRepository.findByGremioId(gremioId);
    }

    @Transactional
    public Formula cambiarEstado(
            Long formulaId,
            EstadoFormula nuevoEstado,
            String email) {

        if (nuevoEstado == null) {
            throw new IllegalArgumentException(
                    "Debes indicar el nuevo estado."
            );
        }

        Usuario usuario = obtenerUsuario(email);

        Formula formula = formulaRepository.findById(formulaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La fórmula no existe."
                ));

        if (!formula.getCreador().getId().equals(usuario.getId())) {
            throw new AccessDeniedException(
                    "Solo el creador puede cambiar el estado de la fórmula."
            );
        }

        EstadoFormula estadoActual = formula.getEstado();

        boolean transicionPermitida =
                (estadoActual == EstadoFormula.ABIERTA
                        && (nuevoEstado == EstadoFormula.EN_VOTACION
                        || nuevoEstado == EstadoFormula.CANCELADA))
                || (estadoActual == EstadoFormula.EN_VOTACION
                        && nuevoEstado == EstadoFormula.CANCELADA);

        if (!transicionPermitida) {
            throw new IllegalStateException(
                    "La transición de " + estadoActual
                            + " a " + nuevoEstado + " no está permitida."
            );
        }

        formula.setEstado(nuevoEstado);

        return formulaRepository.save(formula);
    }

    private Usuario obtenerUsuario(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "El usuario autenticado no existe."
            );
        }

        return usuario;
    }
}
