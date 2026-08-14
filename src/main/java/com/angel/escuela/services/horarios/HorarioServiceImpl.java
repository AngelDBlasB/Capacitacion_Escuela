package com.angel.escuela.services.horarios;

import com.angel.escuela.dto.horarios.HorarioRequest;
import com.angel.escuela.dto.horarios.HorarioResponse;
import com.angel.escuela.entities.Grupo;
import com.angel.escuela.entities.Horario;
import com.angel.escuela.enums.DiaSemana;
import com.angel.escuela.mappers.HorarioMapper;
import com.angel.escuela.repositories.GrupoRepository;
import com.angel.escuela.repositories.HorarioRepository;
import com.angel.escuela.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class HorarioServiceImpl implements HorarioService {

    private final HorarioRepository horarioRepository;
    private final GrupoRepository grupoRepository;

    private final HorarioMapper horarioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HorarioResponse> listar() {
        log.info("Listando horarios");

        return horarioRepository.findAll().stream()
                .map(horarioMapper :: entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HorarioResponse obtenerPorId(Long id) {
        return horarioMapper.entidadAResponse(obtenerHorario(id));
    }

    @Override
    public HorarioResponse registrar(HorarioRequest request) {
        log.info("Request recibido para crear horario: {}", request);

        log.info("Registrando horario");

        DiaSemana dia = obtenerDiaSemanaPorDescripcion(request.dia());
        Grupo grupo = obtenerGrupo(request.idGrupo());

        if (horarioRepository.existeTraslapePorGrupo(grupo.getId(), dia, request.horaInicio(), request.horaFin())) {
            throw new IllegalArgumentException(
                    String.format("El grupo ya tiene una clase asignada el %s en el rango de %s a %s.",
                            dia.getDescripcion(), request.horaInicio(), request.horaFin())
            );
        }

        // 2. Validar si el aula asignada al grupo está ocupada por otro grupo
        if (grupo.getAula() != null && horarioRepository.existeTraslapePorAula(grupo.getAula().getId(), dia, request.horaInicio(), request.horaFin())) {
            throw new IllegalArgumentException(
                    String.format("El aula '%s' ya está ocupada el %s entre las %s y %s.",
                            grupo.getAula().getNombre(), dia.getDescripcion(), request.horaInicio(), request.horaFin())
            );
        }

        Horario horario = horarioMapper.requestAEntidad(request,grupo,dia);

        horarioRepository.save(horario);

        log.info("Horario con ID: {} registrado correctamente", horario.getId());

        return horarioMapper.entidadAResponse(horario);
    }

    @Override
    public HorarioResponse actualizar(HorarioRequest request, Long id) {

        Horario horario = obtenerHorario(id);
        Grupo grupo = obtenerGrupo(request.idGrupo());
        DiaSemana dia = obtenerDiaSemanaPorDescripcion(request.dia());

        log.info("Actualizando horario con ID: {} ", id);

        if (horario.cambioEnDatos(
                grupo,
                dia,
                request.horaInicio(),
                request.horaFin()
        )
        ){
            if (horarioRepository.existeTraslapePorGrupoExcluyendoId(grupo.getId(), dia, request.horaInicio(), request.horaFin(), id)) {
                throw new IllegalArgumentException(
                        String.format("No se puede actualizar: El grupo ya tiene un traslape el %s en el horario %s - %s.",
                                dia.getDescripcion(), request.horaInicio(), request.horaFin())
                );
            }

            // 2. Validar traslape del aula excluyendo el ID del horario actual
            if (grupo.getAula() != null && horarioRepository.existeTraslapePorAulaExcluyendoId(
                    grupo.getAula().getId(), dia, request.horaInicio(), request.horaFin(), id)) {
                throw new IllegalArgumentException(
                        String.format("No se puede actualizar: El aula '%s' ya está ocupada por otro registro el %s de %s a %s.",
                                grupo.getAula().getNombre(), dia.getDescripcion(), request.horaInicio(), request.horaFin())
                );
            }

            horario.actualizar(
                    grupo,
                    dia,
                    request.horaInicio(),
                    request.horaFin()
            );
        }

        return horarioMapper.entidadAResponse(horario);
    }

    @Override
    public void eliminar(Long id) {

        Horario horario = obtenerHorario(id);
        log.info("Eliminando horario con ID: {} ", id);

        horarioRepository.delete(horario);

    }

    private Horario obtenerHorario(Long id) {
        return ServiceUtils.obtenerEntidadOExcepcion(horarioRepository,id, Horario.class);
    }

    private DiaSemana obtenerDiaSemanaPorDescripcion(String descripcion){
        return DiaSemana.obteneDiaSemanaPorDescripcion(descripcion.trim());
    }

    private Grupo obtenerGrupo(Long id) {
        return ServiceUtils.obtenerEntidadOExcepcion(grupoRepository,id, Grupo.class);
    }

}
