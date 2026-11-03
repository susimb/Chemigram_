
package com.chemigram.repository;

import com.chemigram.model.RecompensaAlquimia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecompensaAlquimiaRepository
        extends JpaRepository<RecompensaAlquimia, Long> {

    boolean existsByOpcionId(Long opcionId);
}
