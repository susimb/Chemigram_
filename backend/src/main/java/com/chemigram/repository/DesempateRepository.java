
package com.chemigram.repository;

import com.chemigram.model.Desempate;
import com.chemigram.model.CategoriaOpcion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DesempateRepository
        extends JpaRepository<Desempate, Long> {

    // Consultar todos los desempates de una fórmula.
    List<Desempate> findByFormulaId(Long formulaId);

    // Consultar el desempate de una categoría.
    Optional<Desempate> findByFormulaIdAndCategoria(
            Long formulaId,
            CategoriaOpcion categoria
    );

    // Comprobar si una categoría ya fue desempata.
    boolean existsByFormulaIdAndCategoria(
            Long formulaId,
            CategoriaOpcion categoria
    );
}

