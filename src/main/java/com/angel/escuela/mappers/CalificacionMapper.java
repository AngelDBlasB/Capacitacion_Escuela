package com.angel.escuela.mappers;

import com.angel.escuela.dto.calificaciones.CalificacionRequest;
import com.angel.escuela.dto.calificaciones.CalificacionResponse;
import com.angel.escuela.dto.datos.DatosAlumno;
import com.angel.escuela.dto.datos.DatosGrupo;
import com.angel.escuela.dto.datos.DatosInscripcion;
import com.angel.escuela.entities.*;
import org.springframework.stereotype.Component;

@Component
public class CalificacionMapper implements CommonMapper<CalificacionRequest, CalificacionResponse, Calificacion> {

    @Override
    public Calificacion requestAEntidad(CalificacionRequest request) {

        if (request == null) return null;

        return Calificacion.builder()
                .calificacion(request.calificacion())
                .build();
    }

    public Calificacion requestAEntidad(CalificacionRequest request, Inscripcion inscripcion) {

        if (request == null) return null;

        Calificacion calificacion = requestAEntidad(request);
        calificacion.asignarValores(inscripcion);

        return calificacion;
    }

    @Override
    public CalificacionResponse entidadAResponse(Calificacion entidad) {

        if (entidad == null) return null;

        DatosInscripcion datosInscripcion = entidadADatosInscripcion(entidad.getInscripcion());
        String fechaInscripcion= entidad.getFechaRegistro().toString();

        return new CalificacionResponse(
                entidad.getId(),
                datosInscripcion,
                entidad.getCalificacion(),
                fechaInscripcion
        );
    }

    private DatosInscripcion entidadADatosInscripcion(Inscripcion entidad) {
        if (entidad == null || entidad.getAlumno() == null
                || entidad.getGrupo()==null || entidad.getFechaInscripcion()==null) return null;

        DatosAlumno datosAlumno =entidadADatosAlumno(entidad.getAlumno());
        DatosGrupo datosGrupo = entidadADatosGrupo(entidad.getGrupo());

        return new DatosInscripcion(
                datosAlumno,
                datosGrupo,
                entidad.getFechaInscripcion().toString()
        );
    }

    private DatosAlumno entidadADatosAlumno(Alumno alumno) {
        if (alumno == null || alumno.getNombre() == null || alumno.getApellidoPaterno() == null
                || alumno.getApellidoMaterno() == null || alumno.getEmail() == null
                || alumno.getMatricula() == null|| alumno.getFechaIngreso() == null) return null;

        String nombreCompleto = String.join(" ",
                alumno.getNombre(),
                alumno.getApellidoPaterno(),
                alumno.getApellidoMaterno());

        return new DatosAlumno(
                nombreCompleto,
                alumno.getEmail(),
                alumno.getMatricula(),
                alumno.getFechaIngreso().toString()// != null ? entidad.getFechaIngreso().toString() : null
        );
    }

    private DatosGrupo entidadADatosGrupo(Grupo grupo) {
        if (grupo == null || grupo.getCurso().getNombre() == null
                || grupo.getMaestro().getNombre() == null || grupo.getAula().getNombre() == null
                || grupo.getPeriodo() == null) return null;

        return new DatosGrupo(
                grupo.getCurso().getNombre(),
                grupo.getMaestro().getNombre(),
                grupo.getAula().getNombre(),
                grupo.getPeriodo()
        );
    }
}
