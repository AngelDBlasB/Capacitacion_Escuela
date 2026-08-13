package com.angel.escuela.repositories;

import com.angel.escuela.entities.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HorarioRepository extends JpaRepository<Horario,Long> {

    boolean existsByGrupoId(Long idGrupo);

}
