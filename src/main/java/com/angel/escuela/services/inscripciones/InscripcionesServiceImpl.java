package com.angel.escuela.services.inscripciones;

import com.angel.escuela.dto.grupos.GrupoRequest;
import com.angel.escuela.dto.inscripciones.InscripcionRequest;
import com.angel.escuela.dto.inscripciones.InscripcionResponse;
import com.angel.escuela.entities.*;
import com.angel.escuela.exceptions.EntidadRelacionadaException;
import com.angel.escuela.mappers.InscripcionMapper;
import com.angel.escuela.repositories.CalificacionRepository;
import com.angel.escuela.repositories.GrupoRepository;
import com.angel.escuela.repositories.InscripcionRepository;
import com.angel.escuela.repositories.AlumnoRepository;
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
public class InscripcionesServiceImpl implements InscripcionesService {

    private final InscripcionRepository inscripcionRepository;
    private final GrupoRepository grupoRepository;
    private final AlumnoRepository alumnoRepository;

    private final InscripcionMapper  inscripcionMapper;
    private final CalificacionRepository calificacionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionResponse> listar() {

        log.info("Listando inscripciones");

        return inscripcionRepository.findAll().stream()
                .map(inscripcionMapper :: entidadAResponse).toList();

    }

    @Override
    @Transactional(readOnly = true)
    public InscripcionResponse obtenerPorId(Long id) {
        return inscripcionMapper.entidadAResponse(obtenerInscripcion(id));
    }

    @Override
    public InscripcionResponse registrar(InscripcionRequest request) {

        log.info("Registrando inscripciones...");

        validarDatosUnicos(request);

        Alumno alumno = obtenerAlumno(request.idAlumno());
        Grupo grupo = obtenerGrupo(request.idGrupo());

        Inscripcion inscripcion = inscripcionMapper.requestAEntidad(request,alumno,grupo);

        inscripcionRepository.save(inscripcion);

        log.info("Inscripcion con id: {} registrado correctamente", inscripcion.getId());

        return inscripcionMapper.entidadAResponse(inscripcion);
    }

    @Override
    public InscripcionResponse actualizar(InscripcionRequest request, Long id) {
        Grupo grupo = obtenerGrupo(request.idGrupo());
        Alumno alumno= obtenerAlumno(request.idAlumno());
        Inscripcion inscripcion=obtenerInscripcion(id);

        log.info("Actualizando inscripcion con id {}",id);

        validarCambiosUnicos(request,id);

        if (inscripcion.cambioEnDatos(
                alumno,grupo
        )
        ){
            inscripcion.actualizar(
                    alumno,grupo
            );

            log.info("Datos actualizados para la inscripcion con id {}",id);

        }

        return inscripcionMapper.entidadAResponse(inscripcion);
    }

    @Override
    public void eliminar(Long id) {

        Inscripcion inscripcion = obtenerInscripcion(id);

        log.info("Eliminando grupo con id {}",id);

        if(calificacionRepository.existsByInscripcionId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar la inscripcion ya que tiene calificaciones asignadas");

        inscripcionRepository.delete(inscripcion);

    }

    private Inscripcion obtenerInscripcion(Long id) {
        return ServiceUtils.obtenerEntidadOExcepcion(inscripcionRepository,id, Inscripcion.class);
    }

    private Grupo obtenerGrupo(Long id) {
        return ServiceUtils.obtenerEntidadOExcepcion(grupoRepository,id, Grupo.class);
    }

    private Alumno obtenerAlumno(Long id) {
        return ServiceUtils.obtenerEntidadOExcepcion(alumnoRepository,id, Alumno.class);
    }

    private void validarDatosUnicos(InscripcionRequest request){

        log.info("Validando clave única de grupo...");
        if (inscripcionRepository.existsByAlumnoIdAndGrupoId(
                request.idAlumno(), request.idGrupo()
        )
        )
            throw new IllegalArgumentException("Ya está registrado el alumno en este grupo.");

    }

    private void validarCambiosUnicos(InscripcionRequest request, Long id){

        log.info("Validando clave única de grupo...");
        if (inscripcionRepository.existsByAlumnoIdAndGrupoIdAndIdNot(
                request.idAlumno(),request.idGrupo(), id
        )
        )
            throw new IllegalArgumentException("Ya pertenece al grupo el alumno");

    }

}
