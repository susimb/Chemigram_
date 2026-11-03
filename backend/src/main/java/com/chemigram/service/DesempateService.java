
package com.chemigram.service;

import com.chemigram.model.*;
import com.chemigram.repository.*;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DesempateService {

    private final FormulaRepository formulaRepository;
    private final OpcionFormulaRepository opcionRepository;
    private final MiembroGremioRepository miembroRepository;
    private final DesempateRepository desempateRepository;
    private final ResultadoFormulaService resultadoService;
    private final UsuarioRepository usuarioRepository;

    public DesempateService(
            FormulaRepository formulaRepository,
            OpcionFormulaRepository opcionRepository,
            MiembroGremioRepository miembroRepository,
            DesempateRepository desempateRepository,
            ResultadoFormulaService resultadoService,
            UsuarioRepository usuarioRepository) {

        this.formulaRepository = formulaRepository;
        this.opcionRepository = opcionRepository;
        this.miembroRepository = miembroRepository;
        this.desempateRepository = desempateRepository;
        this.resultadoService = resultadoService;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Desempate resolverEmpate(
            Long formulaId,
            String email,
            Long opcionId) {

        // Identificar al usuario autenticado.
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "El usuario autenticado no existe."
            );
        }

        // Buscar la fórmula.
        Formula formula = formulaRepository.findById(formulaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La fórmula no existe."
                        )
                );

        if (formula.getEstado() != EstadoFormula.EN_VOTACION) {
            throw new IllegalStateException(
                    "La fórmula no está en votación."
            );
        }

        // Obtener el catador oficial del gremio.
        Long gremioId = formula.getGremio().getId();

        MiembroGremio catador = miembroRepository
                .findByGremioIdAndEsCatadorTrue(gremioId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Este gremio no tiene un catador asignado."
                        )
                );

        // Verificar que el usuario autenticado sea el catador.
        if (!catador.getUsuario().getId().equals(usuario.getId())) {
            throw new AccessDeniedException(
                    "Solo el catador oficial puede resolver el empate."
            );
        }

        // Buscar la opción seleccionada.
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

        // Comprobar que la categoría realmente esté empatada.
        ResultadoCategoria resultado = resultadoService
                .calcularResultados(formulaId)
                .stream()
                .filter(r -> r.getCategoria() == opcion.getCategoria())
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No existen votos para esta categoría."
                        )
                );

        if (!resultado.isRequiereDesempate()) {
            throw new IllegalStateException(
                    "Esta categoría no tiene un empate pendiente."
            );
        }

        // Permitir únicamente opciones que estén empatadas.
        boolean opcionEmpatada = resultado.getOpcionesEmpatadas()
                .stream()
                .anyMatch(o -> o.getId().equals(opcionId));

        if (!opcionEmpatada) {
            throw new IllegalArgumentException(
                    "Debes seleccionar una de las opciones empatadas."
            );
        }

        // Evitar que se resuelva dos veces la misma categoría.
        if (desempateRepository.existsByFormulaIdAndCategoria(
                formulaId, opcion.getCategoria())) {

            throw new IllegalStateException(
                    "Esta categoría ya tiene un desempate registrado."
            );
        }

        // Registrar la decisión del catador.
        Desempate desempate = new Desempate(
                formula,
                opcion.getCategoria(),
                opcion,
                catador.getUsuario()
        );

        return desempateRepository.save(desempate);
    }
}

