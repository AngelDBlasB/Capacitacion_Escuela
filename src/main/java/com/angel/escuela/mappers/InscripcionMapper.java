package com.angel.escuela.mappers;

import com.angel.escuela.dto.datos.DatosAlumno;
import com.angel.escuela.dto.datos.DatosGrupo;
import com.angel.escuela.dto.inscripciones.InscripcionRequest;
import com.angel.escuela.dto.inscripciones.InscripcionResponse;
import com.angel.escuela.entities.Alumno;
import com.angel.escuela.entities.Grupo;
import com.angel.escuela.entities.Inscripcion;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class InscripcionMapper implements CommonMapper<InscripcionRequest, InscripcionResponse, Inscripcion> {

    @Override
    public Inscripcion requestAEntidad(InscripcionRequest request) {
        if (request == null) return  null;

        return Inscripcion.builder()
                .build();
    }

    public Inscripcion requestAEntidad(InscripcionRequest request, Alumno alumno, Grupo grupo) {
        if (request == null) return null;

        return Inscripcion.builder()
                .alumno(alumno)
                .grupo(grupo)
                .build();
    }

    @Override
    public InscripcionResponse entidadAResponse(Inscripcion entidad) {

        if (entidad == null) return null;

        DatosAlumno datosAlumno = entidadADatosAlumno(entidad.getAlumno());

        DatosGrupo datosGrupo = entidadADatosGrupo(entidad.getGrupo());

        // 3. Extraer la calificación (si aún no tiene calificación registrada, será null)
        BigDecimal calificacion = entidad.getCalificacion() != null
                ? entidad.getCalificacion().getCalificacion() : BigDecimal.ZERO;

        // 4. Formatear la fecha de inscripción
        String fechaInscripcion= entidad.getFechaInscripcion().toString();// != null
                //? entidad.getFechaInscripcion().toString() : null;

        return new InscripcionResponse(
                entidad.getId(),
                datosAlumno,
                datosGrupo,
                calificacion,
                fechaInscripcion
        );

    }

    private DatosAlumno entidadADatosAlumno(Alumno entidad) {
        if (entidad == null || entidad.getNombre() == null || entidad.getApellidoPaterno() == null
        || entidad.getApellidoMaterno() == null || entidad.getEmail() == null
        || entidad.getMatricula() == null|| entidad.getFechaIngreso() == null) return null;

        String nombreCompleto = String.join(" ",
                entidad.getNombre(),
                entidad.getApellidoPaterno(),
                entidad.getApellidoMaterno());

        return new DatosAlumno(
                nombreCompleto,
                entidad.getEmail(),
                entidad.getMatricula(),
                entidad.getFechaIngreso().toString()// != null ? entidad.getFechaIngreso().toString() : null
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
