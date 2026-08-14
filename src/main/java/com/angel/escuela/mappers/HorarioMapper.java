package com.angel.escuela.mappers;

import com.angel.escuela.dto.datos.DatosCurso;
import com.angel.escuela.dto.datos.DatosGrupo;
import com.angel.escuela.dto.horarios.HorarioRequest;
import com.angel.escuela.dto.horarios.HorarioResponse;
import com.angel.escuela.entities.Curso;
import com.angel.escuela.entities.Grupo;
import com.angel.escuela.entities.Horario;
import com.angel.escuela.enums.DiaSemana;
import org.springframework.stereotype.Component;

@Component
public class HorarioMapper implements CommonMapper<HorarioRequest, HorarioResponse, Horario> {

    @Override
    public Horario requestAEntidad(HorarioRequest request) {

        if (request == null) return  null;

        return Horario.builder()
                .horaInicio(request.horaInicio())
                .horaFin(request.horaFin())
                .build();
    }

    public Horario requestAEntidad(HorarioRequest request, Grupo grupo, DiaSemana dia) {

        if (request == null) return  null;

        Horario horario = requestAEntidad(request);
        horario.asignarValores(grupo, dia);

        return horario;
    }

    @Override
    public HorarioResponse entidadAResponse(Horario entidad) {

        if (entidad == null) return null;

        DatosGrupo datosGrupo = entidadADatosGrupo(entidad.getGrupo());
        String horarioFormato = formatearHorario(entidad);
        return new HorarioResponse(
                entidad.getId(),
                datosGrupo,
                horarioFormato
        );
    }

    private DatosGrupo entidadADatosGrupo(Grupo entidad) {
        if (entidad == null || entidad.getCurso().getNombre() == null
                || entidad.getMaestro().getNombre() == null
                || entidad.getAula().getNombre() == null
                || entidad.getPeriodo() == null) return null;

        return new DatosGrupo(
                entidad.getCurso().getNombre(),
                entidad.getMaestro().getNombre(),
                entidad.getAula().getNombre(),
                entidad.getPeriodo()
        );
    }

    private String formatearHorario(Horario entidad) {
        if (entidad == null || entidad.getDia() == null) return null;

        String dia = entidad.getDia().getDescripcion();
        return String.format("%s %s %s", dia, entidad.getHoraInicio(), entidad.getHoraFin());
    }
}
