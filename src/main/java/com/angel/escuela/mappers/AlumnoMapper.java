package com.angel.escuela.mappers;

import com.angel.escuela.dto.alumnos.AlumnoRequest;
import com.angel.escuela.dto.alumnos.AlumnoResponse;
import com.angel.escuela.dto.datos.DatosCalificacion;
import com.angel.escuela.dto.maestros.MaestroRequest;
import com.angel.escuela.dto.maestros.MaestroResponse;
import com.angel.escuela.entities.Alumno;
import com.angel.escuela.entities.Maestro;
import com.angel.escuela.utils.StringCustomUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AlumnoMapper implements CommonMapper<AlumnoRequest, AlumnoResponse, Alumno> {

    @Override
    public Alumno requestAEntidad(AlumnoRequest request){
        if (request == null) return null;

        return Alumno.builder()
                .nombre(request.nombre().trim())
                .apellidoPaterno(request.apellidoPaterno().trim())
                .apellidoMaterno(request.apellidoMaterno().trim())
                .build();
    }
    public Alumno requestAEntidad(AlumnoRequest request,String email,String matricula){
        if (request == null) return null;

        Alumno alumno = requestAEntidad(request);
        alumno.asignarDatosAcademicos(email,matricula);
        return alumno;
    }

    @Override
    public AlumnoResponse entidadAResponse(Alumno entidad) {

        if (entidad == null) return null;

        List<DatosCalificacion> calificacions = entidadADatosCalificacion(entidad);

        return new AlumnoResponse(
                entidad.getId(),
                String.join(" ",
                        entidad.getNombre(),
                        entidad.getApellidoPaterno(),
                        entidad.getApellidoMaterno()),
                entidad.getEmail(),
                entidad.getMatricula(),
                StringCustomUtils.localDateString(
                        entidad.getFechaIngreso()),
                calificacions,
                entidad.calcularPromedio()
        );

    }

    private List<DatosCalificacion> entidadADatosCalificacion(Alumno entidad){
        if (entidad == null || entidad.getMatricula() == null || entidad.getInscripciones().isEmpty())
            return List.of();

        return entidad.getInscripciones().stream()
                .map(inscripcion -> new DatosCalificacion(
                        inscripcion.getGrupo().getCurso().getNombre(),
                        inscripcion.getGrupo().getPeriodo(),
                        inscripcion.getCalificacion()!= null
                                ? inscripcion.getCalificacion().getCalificacion()
                                : null
                )).toList();
    }

}
