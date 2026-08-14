package com.angel.escuela.dto.calificaciones;

import com.angel.escuela.dto.datos.DatosAlumno;
import com.angel.escuela.dto.datos.DatosGrupo;
import com.angel.escuela.dto.datos.DatosInscripcion;

import java.math.BigDecimal;

public record CalificacionResponse(

        Long id,
        DatosInscripcion inscripcion,
        BigDecimal calificacion,
        String fechaRegistro

) {
}
