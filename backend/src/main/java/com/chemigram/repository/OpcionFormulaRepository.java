
package com.chemigram.repository;

import com.chemigram.model.OpcionFormula;
import com.chemigram.model.CategoriaOpcion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpcionFormulaRepository
        extends JpaRepository<OpcionFormula, Long> {

    // Buscar todas las opciones de una fórmula.
    List<OpcionFormula> findByFormulaId(Long formulaId);

    // Buscar opciones de una categoría específica.
    List<OpcionFormula> findByFormulaIdAndCategoria(
            Long formulaId,
            CategoriaOpcion categoria
    );

    // Buscar las propuestas realizadas por un usuario.
    List<OpcionFormula> findByProponenteId(Long proponenteId);
}
