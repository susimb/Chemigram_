package com.chemigram.repository;

import com.chemigram.model.Gremio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GremioRepository extends JpaRepository<Gremio, Long> {

    List<Gremio> findByCreadorId(Long creadorId);

    boolean existsByNombreIgnoreCase(String nombre);
}
