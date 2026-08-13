package com.angel.escuela.services.aulas;

import com.angel.escuela.dto.aulas.AulaRequest;
import com.angel.escuela.dto.aulas.AulaResponse;
import com.angel.escuela.dto.maestros.MaestroRequest;
import com.angel.escuela.entities.Alumno;
import com.angel.escuela.entities.Aula;
import com.angel.escuela.exceptions.EntidadRelacionadaException;
import com.angel.escuela.mappers.AulaMapper;
import com.angel.escuela.repositories.AulaRepository;
import com.angel.escuela.repositories.GrupoRepository;
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
public class AulaServiceImpl implements AulaService {

    private final AulaRepository aulaRepository;
    private final GrupoRepository grupoRepository;

    private final AulaMapper aulaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AulaResponse> listar() {

        log.info("Listando todas los aulas");

        return aulaRepository.findAll().stream()
                .map(aulaMapper :: entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AulaResponse obtenerPorId(Long id) {

        return aulaMapper.entidadAResponse(obtenerAula(id));

    }

    @Override
    public AulaResponse registrar(AulaRequest request) {

        log.info("Registrando nueva aula...");

        validarDatosUnicos(request);

        Aula aula = aulaMapper.requestAEntidad(request);

        aulaRepository.save(aula);

        log.info("Aula {} registrado correctamente", aula.getNombre());

        return aulaMapper.entidadAResponse(aula);
    }

    @Override
    public AulaResponse actualizar(AulaRequest request, Long id) {
        Aula aula = obtenerAula(id);

        log.info("Actualizando aula con id: {}", id);

        validarCambiosUnicos(request, id);

        if (aula.cambioEnDatos(
                request.nombre().trim(),
                request.capacidad()
        )
        ){
            aula.actualizar(
                    request.nombre(),
                    request.capacidad()
            );

        }

        return aulaMapper.entidadAResponse(aula);
    }

    @Override
    public void eliminar(Long id) {
        Aula aula = obtenerAula(id);

        log.info("Eliminando aula con id: {}", id);

        if(grupoRepository.existsByAulaId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el aula ya que tiene grupos asignados");

        aulaRepository.delete(aula);

    }

    private Aula obtenerAula(Long id) {
        return ServiceUtils.obtenerEntidadOExcepcion(aulaRepository,id,Aula.class);
    }

    private void validarDatosUnicos(AulaRequest request){

        log.info("Validando aula única...");
        if (aulaRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new IllegalArgumentException("Ya existe un aula registrada con el nombre: " + request.nombre());

    }

    private void validarCambiosUnicos(AulaRequest request, Long id){

        log.info("Validando nombre de aula único...");
        if (aulaRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(), id))
            throw new IllegalArgumentException("Ya existe un aula registrado con el nombre: " + request.nombre());

    }
}
