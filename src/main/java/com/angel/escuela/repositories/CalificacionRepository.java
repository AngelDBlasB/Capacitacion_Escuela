package com.angel.escuela.repositories;

import com.angel.escuela.entities.Calificacion;
import com.angel.escuela.entities.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    boolean existsByInscripcionId(Long idInscripcion);

    boolean existsByInscripcionIdAndIdNot(Long idInscripcion, Long id);

}
