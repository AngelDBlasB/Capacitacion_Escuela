package com.angel.escuela.services.calificaciones;

import com.angel.escuela.dto.aulas.AulaRequest;
import com.angel.escuela.dto.calificaciones.CalificacionRequest;
import com.angel.escuela.dto.calificaciones.CalificacionResponse;
import com.angel.escuela.entities.Aula;
import com.angel.escuela.entities.Calificacion;
import com.angel.escuela.entities.Grupo;
import com.angel.escuela.entities.Inscripcion;
import com.angel.escuela.exceptions.EntidadRelacionadaException;
import com.angel.escuela.mappers.CalificacionMapper;
import com.angel.escuela.repositories.CalificacionRepository;
import com.angel.escuela.repositories.InscripcionRepository;
import com.angel.escuela.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CalificacionServiceImpl implements CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final InscripcionRepository inscripcionRepository;

    private final CalificacionMapper calificacionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionResponse> listar() {

        log.info("Listando todas calificaciones");

        return calificacionRepository.findAll().stream()
                .map(calificacionMapper :: entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CalificacionResponse obtenerPorId(Long id) {
        return calificacionMapper.entidadAResponse(obtenerCalificacion(id));
    }

    @Override
    public CalificacionResponse registrar(CalificacionRequest request) {
        log.info("Registrando nueva calificacion...");

        validarDatosUnicos(request);

        validarRangoCalificacion(request.calificacion());

        Inscripcion inscripcion = obtenerInscripcion(request.idInscripcion());
        Calificacion calificacion = calificacionMapper.requestAEntidad(request,inscripcion);

        calificacionRepository.save(calificacion);

        log.info("Calificacion con id: {} registrada correctamente",calificacion.getId());

        return calificacionMapper.entidadAResponse(calificacion);
    }

    @Override
    public CalificacionResponse actualizar(CalificacionRequest request, Long id) {

        Calificacion calificacion = obtenerCalificacion(id);
        Inscripcion inscripcion = obtenerInscripcion(request.idInscripcion());

        log.info("Actualizando calificacion con id: {}",id);

        validarCambiosUnicos(request,id);
        validarRangoCalificacion(request.calificacion());

        if (calificacion.cambioEnDatos(inscripcion,request.calificacion()
        )
        ){
            calificacion.actualizar(
                    inscripcion,request.calificacion()
            );

            log.info("Datos actualizados para la calificacion con id: {}",id);

        }

        return calificacionMapper.entidadAResponse(calificacion);
    }

    @Override
    public void eliminar(Long id) {

        Calificacion calificacion = obtenerCalificacion(id);

        log.info("Eliminando calificacion con id {}",id);

        calificacionRepository.delete(calificacion);

    }

    private Calificacion obtenerCalificacion(Long id) {
        return ServiceUtils.obtenerEntidadOExcepcion(calificacionRepository,id,Calificacion.class);
    }

    private Inscripcion obtenerInscripcion(Long id) {
        return ServiceUtils.obtenerEntidadOExcepcion(inscripcionRepository,id,Inscripcion.class);
    }

    private void validarDatosUnicos(CalificacionRequest request){

        log.info("Validando calificacion única...");
        if (calificacionRepository.existsByInscripcionId(request.idInscripcion()))
            throw new IllegalArgumentException("La inscripcion con el id: " + request.idInscripcion() + " ya tiene agregada una calificacion");

    }

    private void validarCambiosUnicos(CalificacionRequest request, Long id){

        log.info("Validando cambio en calificacion única...");
        if (calificacionRepository.existsByInscripcionIdAndIdNot(request.idInscripcion(),id))
            throw new IllegalArgumentException("La inscripcion con el id: " + request.idInscripcion() + " ya tiene agregada una calificacion");

    }

    private void validarRangoCalificacion(BigDecimal calificacion) {
        if (calificacion == null || calificacion.compareTo(BigDecimal.ZERO) < 0 || calificacion.compareTo(new BigDecimal("10.0")) > 0) {
            throw new IllegalArgumentException("La calificación excede el valor de 10 o es inferior a 0");
        }
    }
}
