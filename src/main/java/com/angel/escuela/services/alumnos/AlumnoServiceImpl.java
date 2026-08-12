package com.angel.escuela.services.alumnos;

import com.angel.escuela.dto.alumnos.AlumnoRequest;
import com.angel.escuela.dto.alumnos.AlumnoResponse;
import com.angel.escuela.entities.Alumno;
import com.angel.escuela.exceptions.EntidadRelacionadaException;
import com.angel.escuela.mappers.AlumnoMapper;
import com.angel.escuela.repositories.AlumnoRepository;
import com.angel.escuela.repositories.InscripcionRepository;
import com.angel.escuela.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final InscripcionRepository inscripcionRepository;

    private final AlumnoMapper alumnoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoResponse> listar() {

        log.info("Listando todos los alumnos");
        return alumnoRepository.findAll().stream()
                .map(alumnoMapper :: entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoResponse obtenerPorId(Long id) {
        return alumnoMapper.entidadAResponse(obtenerAlumno(id));
    }

    @Override
    public AlumnoResponse registrar(AlumnoRequest request) {

        log.info("Registrando nuevo alumno....");

        Alumno alumno = alumnoMapper.requestAEntidad(
                request,
                generarEmail(request),
                generarMatricula(request)
        );

        alumnoRepository.save(alumno);

        log.info("Nuevo alumno {} registrado correctamente",alumno.getNombre());

        return alumnoMapper.entidadAResponse(alumno);
    }

    @Override
    public AlumnoResponse actualizar(AlumnoRequest request, Long id) {

        Alumno alumno = obtenerAlumno(id);

        log.info("Actualizando alumno con id: {}", id);

        if (alumno.cambioEnDatos(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim()
            )
        ){
            alumno.actualizar(
                    request.nombre(),
                    request.apellidoPaterno(),
                    request.apellidoMaterno(),
                    generarEmail(request),
                    generarMatricula(request)
            );

            log.info("Datos academicos regenerados para el alumno con id: {}", id);

        }

        return alumnoMapper.entidadAResponse(alumno);
    }

    @Override
    public void eliminar(Long id) {

        Alumno alumno = obtenerAlumno(id);

        log.info("Eliminando alumno con id: {}", id);

        if(inscripcionRepository.existsByAlumnoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el alumno ya que tiene inscripciones asignadas");

        alumnoRepository.delete(alumno);

        log.info("Alumno con id {} eliminado", id);

    }

    private Alumno obtenerAlumno(Long id) {
        return ServiceUtils.obtenerEntidadOExcepcion(alumnoRepository, id, Alumno.class);
    }

    private String generarMatricula(AlumnoRequest request) {
        log.info("Generando matricula...");
        String matricula = alumnoRepository.generarMatricula(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim()
        );
        log.info("Matricula generada: {}", matricula);

        return matricula;
    }

    private String generarEmail(AlumnoRequest request) {
        log.info("Generando email...");
        String email = alumnoRepository.generarEmail(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim()
        );
        log.info("Email generado: {}", email);

        return email;
    }
}
