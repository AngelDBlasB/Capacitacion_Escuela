package com.angel.escuela.services.cursos;

import com.angel.escuela.dto.aulas.AulaRequest;
import com.angel.escuela.dto.cursos.CursoRequest;
import com.angel.escuela.dto.cursos.CursoResponse;
import com.angel.escuela.entities.Aula;
import com.angel.escuela.entities.Curso;
import com.angel.escuela.exceptions.EntidadRelacionadaException;
import com.angel.escuela.mappers.CursoMapper;
import com.angel.escuela.repositories.CursoRepository;
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
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final GrupoRepository grupoRepository;

    private final CursoMapper cursoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponse> listar() {

        log.info("Listando todos los Cursos");
        return cursoRepository.findAll().stream()
                .map(cursoMapper::entidadAResponse).toList();

    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponse obtenerPorId(Long id) {

        return cursoMapper.entidadAResponse(obtenerCurso(id));

    }

    @Override
    public CursoResponse registrar(CursoRequest request) {

        log.info("Registrando curso...");

        validarDatosUnicos(request);

        Curso curso = cursoMapper.requestAEntidad(request);

        cursoRepository.save(curso);

        log.info("Curso {} creado correctamente",curso.getNombre());

        return cursoMapper.entidadAResponse(curso);
    }

    @Override
    public CursoResponse actualizar(CursoRequest request, Long id) {

        Curso curso = obtenerCurso(id);

        log.info("Actualizando aula con id: {}", id);

        validarCambiosUnicos(request, id);

        if (curso.cambioEnDatos(
                request.nombre().trim(),
                request.descripcion().trim(),
                request.creditos()
        )
        ){
            curso.actualizar(
                    request.nombre(),
                    request.descripcion(),
                    request.creditos()
            );

        }

        return cursoMapper.entidadAResponse(curso);

    }

    @Override
    public void eliminar(Long id) {

        Curso curso = obtenerCurso(id);

        log.info("Eliminando curso con id: {}", id);

        if(grupoRepository.existsByCursoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el curso ya que tiene grupos asignados");

        cursoRepository.delete(curso);

    }

    private Curso obtenerCurso(Long id) {
        return ServiceUtils.obtenerEntidadOExcepcion(cursoRepository,id,Curso.class);
    }

    private void validarDatosUnicos(CursoRequest request){

        log.info("Validando curso única...");
        if (cursoRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new IllegalArgumentException("Ya existe un curso registrado con el nombre: " + request.nombre());

    }

    private void validarCambiosUnicos(CursoRequest request, Long id){

        log.info("Validando nombre del curso único...");
        if (cursoRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(), id))
            throw new IllegalArgumentException("Ya existe un curso registrado con el nombre: " + request.nombre());

    }
}
