package com.angel.escuela.mappers;

import com.angel.escuela.dto.datos.DatosAulas;
import com.angel.escuela.dto.datos.DatosCurso;
import com.angel.escuela.dto.datos.DatosMaestro;
import com.angel.escuela.dto.grupos.GrupoRequest;
import com.angel.escuela.dto.grupos.GrupoResponse;
import com.angel.escuela.entities.Aula;
import com.angel.escuela.entities.Curso;
import com.angel.escuela.entities.Grupo;
import com.angel.escuela.entities.Maestro;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GrupoMapper implements CommonMapper<GrupoRequest, GrupoResponse, Grupo> {


    @Override
    public Grupo requestAEntidad(GrupoRequest request) {
        if (request == null) return  null;

        return Grupo.builder()
                .periodo(request.periodo())
                .build();
    }

    public Grupo requestAEntidad(GrupoRequest request, Curso curso, Maestro maestro, Aula aula) {
        if (request == null) return null;

        Grupo grupo = requestAEntidad(request);
        grupo.asignarRelaciones(curso, maestro, aula);

        return grupo;
    }

    @Override
    public GrupoResponse entidadAResponse(Grupo entidad) {

        if (entidad == null) return null;

        DatosCurso datosCurso = entidadADatosCurso(entidad.getCurso());
        DatosMaestro datosMaestro = entidadADatosMaestro(entidad.getMaestro());
        DatosAulas datosAula = entidadADatosAula(entidad.getAula());
        List<String> horarios = entidadAHorarios(entidad);

        return new GrupoResponse(
                entidad.getId(),
                datosCurso,
                datosMaestro,
                datosAula,
                horarios,
                entidad.getPeriodo()
        );

    }

    private DatosCurso entidadADatosCurso(Curso entidad) {
        if (entidad == null || entidad.getNombre() == null
                || entidad.getCreditos()==null) return null;

        return new DatosCurso(
                entidad.getNombre(),
                entidad.getDescripcion(),
                entidad.getCreditos()
        );
    }

    private DatosMaestro entidadADatosMaestro(Maestro entidad) {
        if (entidad == null || entidad.getNombre() == null
                || entidad.getEmail() == null || entidad.getTelefono() == null)
            return null;

        String nombreCompleto = String.join(" ",
                entidad.getNombre(),
                entidad.getApellidoPaterno(),
                entidad.getApellidoMaterno());

        return new DatosMaestro(
                nombreCompleto,
                entidad.getEmail(),
                entidad.getTelefono()
        );
    }

    private DatosAulas entidadADatosAula(Aula entidad) {
        if (entidad == null || entidad.getNombre() == null
                || entidad.getCapacidad() == null) return null;

        return new DatosAulas(
                entidad.getNombre(),
                entidad.getCapacidad()
        );
    }

    private List<String> entidadAHorarios(Grupo entidad) {
        if (entidad == null || entidad.getHorarios() == null || entidad.getHorarios().isEmpty()) {
            return List.of();
        }

        return entidad.getHorarios().stream()
                .map(horario -> String.format("%s %s - %s",
                        horario.getDia(),
                        horario.getHoraInicio(),
                        horario.getHoraFin()
                )
                )
                .toList();
    }
}
