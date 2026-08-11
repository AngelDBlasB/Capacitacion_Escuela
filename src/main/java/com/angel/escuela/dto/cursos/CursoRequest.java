package com.angel.escuela.dto.cursos;

import jakarta.validation.constraints.*;

public record CursoRequest(

        @NotBlank(message = "El nombre es requerido")
        @Size(min = 5, max = 100, message = "El nombre debe tener entre 5 y 100 caracteres")
        String nombre,

        @Size(max = 200, message = "La descripción debe tener máximo y 200 caracteres")
        String descripcion,

        @NotNull(message = "Los créditos son requeridos")
        @Min(value = 1, message = "Los créditos mínimos son 1")
        @Max(value = 10, message = "Los créditos máximos son 10")
        Integer creditos

) {
}
