package com.angel.escuela.repositories;

import com.angel.escuela.entities.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    boolean existsByAlumnoId(Long idAlumno);
    boolean existsByGrupoId(Long idGrupo);

    boolean existsByAlumnoIdAndGrupoId(Long idAlumno, Long idGrupo);

    boolean existsByAlumnoIdAndGrupoIdAndIdNot(Long idAlumno, Long idGrupo, Long idInscripcion);

}
