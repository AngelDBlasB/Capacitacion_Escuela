package com.angel.escuela.repositories;

import com.angel.escuela.entities.Horario;
import com.angel.escuela.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HorarioRepository extends JpaRepository<Horario,Long> {

    boolean existsByGrupoId(Long idGrupo);

    // --- QUERIES PARA REGISTRAR ---

    @Query("""
        SELECT COUNT(h) > 0 FROM Horario h 
        WHERE h.grupo.id = :idGrupo 
          AND h.dia = :diaSemana
          AND (:horaInicio < h.horaFin AND :horaFin > h.horaInicio)
    """)
    boolean existeTraslapePorGrupo(
            @Param("idGrupo") Long idGrupo,
            @Param("diaSemana") DiaSemana diaSemana,
            @Param("horaInicio") String horaInicio,
            @Param("horaFin") String horaFin
    );

    @Query("""
        SELECT COUNT(h) > 0 FROM Horario h 
        WHERE h.grupo.aula.id = :idAula 
          AND h.dia = :diaSemana
          AND (:horaInicio < h.horaFin AND :horaFin > h.horaInicio)
    """)
    boolean existeTraslapePorAula(
            @Param("idAula") Long idAula,
            @Param("diaSemana") DiaSemana diaSemana,
            @Param("horaInicio") String horaInicio,
            @Param("horaFin") String horaFin
    );

    // --- QUERIES PARA ACTUALIZAR ---

    @Query("""
        SELECT COUNT(h) > 0 FROM Horario h 
        WHERE h.grupo.id = :idGrupo 
          AND h.dia = :diaSemana
          AND h.id != :idHorario
          AND (:horaInicio < h.horaFin AND :horaFin > h.horaInicio)
    """)
    boolean existeTraslapePorGrupoExcluyendoId(
            @Param("idGrupo") Long idGrupo,
            @Param("diaSemana") DiaSemana diaSemana,
            @Param("horaInicio") String horaInicio,
            @Param("horaFin") String horaFin,
            @Param("idHorario") Long idHorario
    );

    @Query("""
        SELECT COUNT(h) > 0 FROM Horario h 
        WHERE h.grupo.aula.id = :idAula 
          AND h.dia = :diaSemana
          AND h.id != :idHorario
          AND (:horaInicio < h.horaFin AND :horaFin > h.horaInicio)
    """)
    boolean existeTraslapePorAulaExcluyendoId(
            @Param("idAula") Long idAula,
            @Param("diaSemana") DiaSemana diaSemana,
            @Param("horaInicio") String horaInicio,
            @Param("horaFin") String horaFin,
            @Param("idHorario") Long idHorario
    );

}
