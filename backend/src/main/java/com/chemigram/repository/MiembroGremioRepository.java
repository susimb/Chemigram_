
package com.chemigram.repository;

import com.chemigram.model.MiembroGremio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MiembroGremioRepository
        extends JpaRepository<MiembroGremio, Long> {

    List<MiembroGremio> findByGremioId(Long gremioId);

    List<MiembroGremio> findByUsuarioId(Long usuarioId);

    Optional<MiembroGremio> findByUsuarioIdAndGremioId(
            Long usuarioId,
            Long gremioId
    );

    Optional<MiembroGremio> findByGremioIdAndEsCatadorTrue(
            Long gremioId
    );

    boolean existsByUsuarioIdAndGremioId(
            Long usuarioId,
            Long gremioId
    );
}
