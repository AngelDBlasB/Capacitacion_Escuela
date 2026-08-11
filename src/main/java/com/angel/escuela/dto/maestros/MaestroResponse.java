package com.angel.escuela.dto.maestros;

import com.angel.escuela.dto.datos.DatosCurso;

import java.util.List;

public record MaestroResponse(

        Long id,
        String nombre,
        String email,
        String telefono,
        List<DatosCurso> cursos

) {
}
