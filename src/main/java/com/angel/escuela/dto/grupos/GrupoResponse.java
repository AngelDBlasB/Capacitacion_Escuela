package com.angel.escuela.dto.grupos;

import com.angel.escuela.dto.aulas.AulaResponse;
import com.angel.escuela.dto.cursos.CursoResponse;
import com.angel.escuela.dto.datos.DatosAulas;
import com.angel.escuela.dto.datos.DatosCurso;
import com.angel.escuela.dto.datos.DatosMaestro;
import com.angel.escuela.dto.maestros.MaestroResponse;

import java.util.List;

public record GrupoResponse(

    Long id,
    DatosCurso curso,
    DatosMaestro maestro,
    DatosAulas aula,
    List<String> horario,
    String perido

) {
}
