package com.angel.escuela.dto.grupos;

import jakarta.validation.constraints.*;

public record GrupoRequest(

        @NotNull(message = "El ID de la sucursal es requerido")
        @Positive(message = "El ID de la sucursal debe ser positivo")
        Long idCurso,

        @NotNull(message = "El ID de la sucursal es requerido")
        @Positive(message = "El ID de la sucursal debe ser positivo")
        Long idMaestro,

        @NotNull(message = "El ID de la sucursal es requerido")
        @Positive(message = "El ID de la sucursal debe ser positivo")
        Long idAula,

        @NotBlank(message = "El perido es requerido")
        //@Size(min = 1, max = 20, message = "El perido debe tener entre 1 y 20 caracteres")
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "El periodo debe tener el formato YYYY-MM")
        String periodo

) {
}
