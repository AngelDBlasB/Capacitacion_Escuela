package com.angel.escuela.dto.alumnos;

import com.angel.escuela.dto.datos.DatosCalificacion;

import java.math.BigDecimal;
import java.util.List;

public record AlumnoResponse(

        Long id,
        String nombre,
        String email,
        String matricula,
        String fechaIngreso,
        List<DatosCalificacion> calificaciones,
        BigDecimal promedio

) {
}
