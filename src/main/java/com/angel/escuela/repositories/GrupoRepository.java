package com.angel.escuela.repositories;

import com.angel.escuela.entities.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    boolean existsByMaestroId(Long idMaestro);
    boolean existsByAulaId(Long idAula);
}
