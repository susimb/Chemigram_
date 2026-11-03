
package com.chemigram.repository;

import com.chemigram.model.Voto;
import com.chemigram.model.CategoriaOpcion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    // Buscar todos los votos de una fórmula.
    List<Voto> findByFormulaId(Long formulaId);

    // Buscar votos de una categoría específica.
    List<Voto> findByFormulaIdAndCategoria(
            Long formulaId,
            CategoriaOpcion categoria
    );

    // Consultar si un usuario ya votó en esa categoría.
    Optional<Voto> findByFormulaIdAndCategoriaAndUsuarioId(
            Long formulaId,
            CategoriaOpcion categoria,
            Long usuarioId
    );

    // Contar votos recibidos por una opción.
    long countByOpcionId(Long opcionId);
}
