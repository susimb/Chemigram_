
package com.chemigram.repository;

import com.chemigram.model.Pocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PocionRepository extends JpaRepository<Pocion, Long> {

    @Query("""
        SELECT COALESCE(SUM(p.rareza), 0)
        FROM Pocion p
        WHERE p.creador.id = :usuarioId
    """)
    Long sumarRarezaPorUsuario(@Param("usuarioId") Long usuarioId);
    boolean existsByFormulaId(Long formulaId);
}
