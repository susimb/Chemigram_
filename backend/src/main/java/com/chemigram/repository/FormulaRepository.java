
package com.chemigram.repository;

import com.chemigram.model.Formula;
import com.chemigram.model.EstadoFormula;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormulaRepository extends JpaRepository<Formula, Long> {

    List<Formula> findByGremioId(Long gremioId);

    List<Formula> findByCreadorId(Long creadorId);

    List<Formula> findByGremioIdAndEstado(
            Long gremioId,
            EstadoFormula estado
    );

    boolean existsByNombreIgnoreCaseAndGremioId(
            String nombre,
            Long gremioId
    );
}
