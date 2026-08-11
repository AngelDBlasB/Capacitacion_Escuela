package com.angel.escuela.enums;

import com.angel.escuela.exceptions.RecursoNoEncontradoException;
import com.angel.escuela.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DiaSemana {

    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miércoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sábado");

    private final String descripcion;
    public static DiaSemana obteneDiaSemanaPorDescripcion(String descripcion){

        StringCustomUtils.validarNoVacio(descripcion,"La descripción es requerida");

        String descripcionNormalizada = StringCustomUtils.quitarAcentos(descripcion);

        for(DiaSemana diasemana : values()){
            if (StringCustomUtils.quitarAcentos(diasemana.descripcion).equalsIgnoreCase(descripcionNormalizada))
                return diasemana;
        }

        throw new RecursoNoEncontradoException("No existe un día de la semana con la descripción: " + descripcion);

    }


}
