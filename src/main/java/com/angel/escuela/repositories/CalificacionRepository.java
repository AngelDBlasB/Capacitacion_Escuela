package com.angel.escuela.repositories;

import com.angel.escuela.entities.Calificacion;
import com.angel.escuela.entities.Inscripcion;
import org.springframework.data.repository.CrudRepository;

public interface CalificacionRepository extends CrudRepository<Calificacion, Long> {

    boolean existsByInscripcionId(Long idInscripcion);

}
