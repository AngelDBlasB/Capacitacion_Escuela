package com.angel.escuela.services.maestros;

import com.angel.escuela.dto.maestros.MaestroRequest;
import com.angel.escuela.dto.maestros.MaestroResponse;
import com.angel.escuela.entities.Maestro;
import com.angel.escuela.exceptions.EntidadRelacionadaException;
import com.angel.escuela.mappers.MaestroMapper;
import com.angel.escuela.repositories.GrupoRepository;
import com.angel.escuela.repositories.MaestroRepository;
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
public class MaestroServiceImpl implements MaestroService{

    private final MaestroRepository maestroRepository;
    private final GrupoRepository grupoRepository;

    private final MaestroMapper maestroMapper;

    @Override
    public List<MaestroResponse> listar() {

        log.info("Listando todos los maestros");
        return maestroRepository.findAll().stream()
                .map(maestroMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MaestroResponse obtenerPorId(Long id) {
        return maestroMapper.entidadAResponse(obtenerMaestro(id));
    }

    @Override
    public MaestroResponse registrar(MaestroRequest request) {

        log.info("Registrando nuevo maestro...");

        validarDatosUnicos(request);

        Maestro maestro = maestroMapper.requestAEntidad(request);

        maestroRepository.save(maestro);

        log.info("Nuevo maestro {} registrado",maestro.getNombre());

        return maestroMapper.entidadAResponse(maestro);
    }

    @Override
    public MaestroResponse actualizar(MaestroRequest request, Long id) {

        Maestro maestro = obtenerMaestro(id);

        log.info("Actualizando maestro con id: {}", id);

        validarCambiosUnicos(request, id);

        maestro.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono()
        );

        log.info("Maestro {} actualizado correctamente", maestro.getNombre());

        return maestroMapper.entidadAResponse(maestro);
    }

    @Override
    public void eliminar(Long id) {

        Maestro maestro = obtenerMaestro(id);

        log.info("Eliminando maestro con id: {}", id);

        if(grupoRepository.existsByMaestroId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el maestro ya que tiene grupos asignados");

        maestroRepository.delete(maestro);

        log.info("Maestro {} eliminado correctamente", maestro.getNombre());

    }

    private Maestro obtenerMaestro(Long id){
        return ServiceUtils.obtenerEntidadOExcepcion(maestroRepository,id,Maestro.class);
    }

    private void validarDatosUnicos(MaestroRequest request){

        log.info("Validando email único...");
        if (maestroRepository.existsByEmailIgnoreCase(request.email().trim()))
            throw new IllegalArgumentException("Ya existe un maestro registrado con el email: " + request.email());

        log.info("Validando teléfono único...");
        if (maestroRepository.existsByTelefono(request.telefono().trim()))
            throw new IllegalArgumentException("Ya existe un maestro registrado con el teléfono: " + request.telefono());

    }

    private void validarCambiosUnicos(MaestroRequest request, Long id){

        log.info("Validando email único...");
        if (maestroRepository.existsByEmailIgnoreCaseAndIdNot(request.email().trim(), id))
            throw new IllegalArgumentException("Ya existe un maestro registrado con el email: " + request.email());

        log.info("Validando teléfono único...");
        if (maestroRepository.existsByTelefonoAndIdNot(request.telefono().trim(), id))
            throw new IllegalArgumentException("Ya existe un maestro registrado con el teléfono: " + request.telefono());

    }
}
