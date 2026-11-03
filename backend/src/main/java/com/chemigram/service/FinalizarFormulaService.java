
package com.chemigram.service;

import com.chemigram.model.*;
import com.chemigram.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FinalizarFormulaService {

    private final PocionRepository pocionRepository;
    private final FormulaRepository formulaRepository;
    private final ResultadoFormulaService resultadoService;
    private final DesempateRepository desempateRepository;
    private final PocionService pocionService;
    private final UsuarioRepository usuarioRepository;
    private final RecompensaAlquimiaRepository recompensaRepository;

    public FinalizarFormulaService(
            FormulaRepository formulaRepository,
            ResultadoFormulaService resultadoService,
            DesempateRepository desempateRepository,
            PocionService pocionService,
            PocionRepository pocionRepository,
            UsuarioRepository usuarioRepository,
            RecompensaAlquimiaRepository recompensaRepository) {

        this.formulaRepository = formulaRepository;
        this.resultadoService = resultadoService;
        this.desempateRepository = desempateRepository;
        this.pocionService = pocionService;
        this.pocionRepository = pocionRepository;
        this.usuarioRepository = usuarioRepository;
        this.recompensaRepository = recompensaRepository;
    }

    @Transactional
    public Pocion finalizarFormula(Long formulaId, String email) {

        Formula formula = formulaRepository.findById(formulaId)
                .orElseThrow(() ->
                        new IllegalArgumentException("La fórmula no existe.")
                );

        if (formula.getEstado() == EstadoFormula.FINALIZADA
                || pocionRepository.existsByFormulaId(formulaId)) {
            throw new IllegalStateException(
                    "Esta fórmula ya fue finalizada o ya tiene una poción asociada."
            );
        }

        if (formula.getEstado() != EstadoFormula.EN_VOTACION) {
            throw new IllegalStateException(
                    "La fórmula no está en votación."
            );
        }

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "El usuario autenticado no existe."
            );
        }

        if (!formula.getCreador().getId().equals(usuario.getId())) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Solo el creador de la fórmula puede finalizarla."
            );
        }

        List<ResultadoCategoria> resultados =
                resultadoService.calcularResultados(formulaId);

        Map<CategoriaOpcion, OpcionFormula> ganadores =
                new EnumMap<>(CategoriaOpcion.class);

        for (ResultadoCategoria resultado : resultados) {

            CategoriaOpcion categoria = resultado.getCategoria();

            if (resultado.isRequiereDesempate()) {

                Desempate desempate = desempateRepository
                        .findByFormulaIdAndCategoria(formulaId, categoria)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Falta resolver el empate de " + categoria
                                )
                        );

                ganadores.put(categoria, desempate.getOpcionGanadora());

            } else {

                ganadores.put(
                        categoria,
                        resultado.getOpcionGanadora()
                );
            }
        }

        for (CategoriaOpcion categoria : CategoriaOpcion.values()) {

            if (!ganadores.containsKey(categoria)) {
                throw new IllegalStateException(
                        "Falta un resultado para la categoría " + categoria
                );
            }
        }

        String nombrePocion = formula.getNombre();

        String descripcionPocion =
                "Poción creada a partir de la fórmula: "
                        + formula.getNombre()
                        + ". Ingrediente: "
                        + ganadores.get(CategoriaOpcion.INGREDIENTE).getContenido()
                        + ". Método: "
                        + ganadores.get(CategoriaOpcion.METODO).getContenido()
                        + ". Frasco: "
                        + ganadores.get(CategoriaOpcion.FRASCO).getContenido();

        // Crear la poción.
        // La rareza permanece provisionalmente en 0.
        Pocion pocion = pocionService.crearPocion(
                nombrePocion,
                descripcionPocion,
                0,
                formula.getCreador().getId()
        );

        pocion.setFormula(formula);
        pocionRepository.save(pocion);

        // Recompensar con 5 puntos al proponente de cada opción ganadora.
        for (OpcionFormula opcionGanadora : ganadores.values()) {

            Long opcionId = opcionGanadora.getId();

            if (!recompensaRepository.existsByOpcionId(opcionId)) {

                Usuario proponente = opcionGanadora.getProponente();

                RecompensaAlquimia recompensa = new RecompensaAlquimia(
                        proponente,
                        formula,
                        opcionGanadora,
                        5
                );

                recompensaRepository.save(recompensa);

                proponente.setPuntosAlquimia(
                        proponente.getPuntosAlquimia() + 5
                );

                usuarioRepository.save(proponente);
            }
        }

        // Recompensar con 10 puntos al creador de la fórmula.
        Usuario creador = formula.getCreador();

        creador.setPuntosAlquimia(
                creador.getPuntosAlquimia() + 10
        );

        usuarioRepository.save(creador);

        // Marcar la fórmula como finalizada.
        formula.setEstado(EstadoFormula.FINALIZADA);
        formulaRepository.save(formula);

        return pocion;
    }
}

