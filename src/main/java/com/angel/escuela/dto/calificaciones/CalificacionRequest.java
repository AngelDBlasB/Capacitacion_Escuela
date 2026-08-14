package com.angel.escuela.dto.calificaciones;

import java.math.BigDecimal;

public record CalificacionRequest(

        Long idInscripcion,
        BigDecimal calificacion

) {
}
