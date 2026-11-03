package com.chemigram.service;

import com.chemigram.model.*;
import com.chemigram.repository.*;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OpcionFormulaService {

    private final OpcionFormulaRepository opcionRepository;
    private final FormulaRepository formulaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MiembroGremioRepository miembroRepository;

    public OpcionFormulaService(
            OpcionFormulaRepository opcionRepository,
            FormulaRepository formulaRepository,
            UsuarioRepository usuarioRepository,
            MiembroGremioRepository miembroRepository) {

        this.opcionRepository = opcionRepository;
        this.formulaRepository = formulaRepository;
        this.usuarioRepository = usuarioRepository;
        this.miembroRepository = miembroRepository;
    }

    @Transactional
    public OpcionFormula crearOpcion(
            String contenido,
            CategoriaOpcion categoria,
            Long formulaId,
            String email) {

        if (contenido == null || contenido.isBlank()) {
            throw new IllegalArgumentException(
                    "El contenido de la propuesta no puede estar vacío."
            );
        }

        if (categoria == null) {
            throw new IllegalArgumentException(
                    "Debes seleccionar una categoría."
            );
        }

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "El usuario autenticado no existe."
            );
        }

        Formula formula = obtenerFormulaDeMiembro(
                formulaId,
                usuario
        );

        if (formula.getEstado() != EstadoFormula.ABIERTA) {
            throw new IllegalStateException(
                    "Solo puedes proponer opciones en fórmulas abiertas."
            );
        }

        OpcionFormula opcion = new OpcionFormula(
                contenido.trim(),
                categoria,
                formula,
                usuario
        );

        return opcionRepository.save(opcion);
    }

    @Transactional(readOnly = true)
    public List<OpcionFormula> listarPorFormula(
            Long formulaId,
            String email) {

        Formula formula = obtenerFormulaDeMiembro(
                formulaId,
                obtenerUsuario(email)
        );

        return opcionRepository.findByFormulaId(formula.getId());
    }

    @Transactional(readOnly = true)
    public List<OpcionFormula> listarPorCategoria(
            Long formulaId,
            CategoriaOpcion categoria,
            String email) {

        if (categoria == null) {
            throw new IllegalArgumentException(
                    "Debes seleccionar una categoría."
            );
        }

        Formula formula = obtenerFormulaDeMiembro(
                formulaId,
                obtenerUsuario(email)
        );

        return opcionRepository.findByFormulaIdAndCategoria(
                formula.getId(),
                categoria
        );
    }

    @Transactional(readOnly = true)
    public List<OpcionFormula> listarPorUsuario(
            Long usuarioId,
            String email) {

        Usuario usuarioAutenticado = obtenerUsuario(email);

        if (!usuarioAutenticado.getId().equals(usuarioId)) {
            throw new AccessDeniedException(
                    "Solo puedes consultar tus propias propuestas."
            );
        }

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new IllegalArgumentException(
                    "El usuario no existe."
            );
        }

        return opcionRepository.findByProponenteId(usuarioId);
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

    private Formula obtenerFormulaDeMiembro(
            Long formulaId,
            Usuario usuario) {

        Formula formula = formulaRepository.findById(formulaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La fórmula no existe."
                        )
                );

        Long gremioId = formula.getGremio().getId();

        boolean esMiembro = miembroRepository
                .existsByUsuarioIdAndGremioId(
                        usuario.getId(),
                        gremioId
                );

        if (!esMiembro) {
            throw new AccessDeniedException(
                    "Debes pertenecer al gremio para acceder a estas opciones."
            );
        }

        return formula;
    }
}


