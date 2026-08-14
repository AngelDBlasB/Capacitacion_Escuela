package com.angel.escuela.dto.inscripciones;

import com.angel.escuela.dto.datos.DatosAlumno;
import com.angel.escuela.dto.datos.DatosGrupo;

import java.math.BigDecimal;

public record InscripcionResponse(

        Long id,
        DatosAlumno alumno,
        DatosGrupo grupo,
        BigDecimal calificacion,
        String fechaInscripcion

) {
}
