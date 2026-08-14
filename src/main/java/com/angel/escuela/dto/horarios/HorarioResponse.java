package com.angel.escuela.dto.horarios;

import com.angel.escuela.dto.datos.DatosGrupo;

public record HorarioResponse(

        Long id,
        DatosGrupo grupo,
        String horario

) {
}
