package com.angel.escuela.dto.aulas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AulaRequest(

        @NotBlank(message = "El nombre es requerido")
        @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
        String nombre,

        @NotNull(message = "La capacidad es requerida")
        @Positive(message = "La capacidad debe ser un número positivo")
        Integer capacidad

) {
}
