package com.angel.escuela.services.grupos;

import com.angel.escuela.dto.cursos.CursoRequest;
import com.angel.escuela.dto.grupos.GrupoRequest;
import com.angel.escuela.dto.grupos.GrupoResponse;
import com.angel.escuela.entities.Aula;
import com.angel.escuela.entities.Curso;
import com.angel.escuela.entities.Grupo;
import com.angel.escuela.entities.Maestro;
import com.angel.escuela.exceptions.EntidadRelacionadaException;
import com.angel.escuela.mappers.GrupoMapper;
import com.angel.escuela.repositories.*;
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
public class GruposServiceImpl implements GruposService {

    private final GrupoRepository grupoRepository;
    private final AulaRepository aulaRepository;
    private final CursoRepository cursoRepository;
    private final MaestroRepository maestroRepository;
    private final InscripcionRepository  inscripcionRepository;
    private final HorarioRepository horarioRepository;

    private final GrupoMapper grupoMapper;


    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponse> listar() {
        log.info("Iniciando lista de grupos");
        return grupoRepository.findAll().stream()
                .map(grupoMapper :: entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoResponse obtenerPorId(Long id) {
        return grupoMapper.entidadAResponse(obtenerGrupo(id));
    }

    @Override
    public GrupoResponse registrar(GrupoRequest request) {

        log.info("Registrando grupo...");

        validarDatosUnicos(request);

        Curso curso = obtenerCurso(request.idCurso());
        Maestro maestro = obtenerMaestro(request.idMaestro());
        Aula aula = obtenerAula(request.idAula());

        Grupo grupo = grupoMapper.requestAEntidad(request,curso,maestro,aula);

        grupoRepository.save(grupo);

        log.info("Grupo con id: {} creado correctamente",grupo.getId());

        return grupoMapper.entidadAResponse(grupo);
    }

    @Override
    public GrupoResponse actualizar(GrupoRequest request, Long id) {

        Grupo grupo = obtenerGrupo(id);
        Curso curso = obtenerCurso(request.idCurso());
        Maestro maestro = obtenerMaestro(request.idMaestro());
        Aula aula = obtenerAula(request.idAula());

        log.info("Actualizando grupo con id {}",id);

        validarCambiosUnicos(request,id);

        if (grupo.cambioEnDatos(
                curso,
                maestro,
                aula,
                request.periodo()
        )
        ){
            grupo.actualizar(
                    curso,
                    maestro,
                    aula,
                    request.periodo()
            );

            log.info("Datos actualizados para el grupo con id {}",id);

        }

        return grupoMapper.entidadAResponse(grupo);
    }

    @Override
    public void eliminar(Long id) {

        Grupo grupo = obtenerGrupo(id);

        log.info("Eliminando grupo con id {}",id);

        if(inscripcionRepository.existsByGrupoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el grupo ya que tiene inscripciones asignadas");

        if(horarioRepository.existsByGrupoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el grupo ya que tiene horarios asignados");

        grupoRepository.delete(grupo);
    }

    private Grupo obtenerGrupo(Long id) {
        return ServiceUtils.obtenerEntidadOExcepcion(grupoRepository,id, Grupo.class);
    }

    private Curso obtenerCurso(Long id) {
        return ServiceUtils.obtenerEntidadOExcepcion(cursoRepository,id,Curso.class);
    }

    private Maestro obtenerMaestro(Long id){
        return ServiceUtils.obtenerEntidadOExcepcion(maestroRepository,id,Maestro.class);
    }

    private Aula obtenerAula(Long id) {
        return ServiceUtils.obtenerEntidadOExcepcion(aulaRepository,id,Aula.class);
    }

    private void validarDatosUnicos(GrupoRequest request){

        log.info("Validando clave única de grupo...");
        if (grupoRepository.existsByCursoIdAndMaestroIdAndAulaIdAndPeriodo(
                request.idCurso(),request.idMaestro(),request.idAula(),request.periodo()
            )
        )
            throw new IllegalArgumentException("Ya existe un grupo registrado con el idCurso: " + request.idCurso()
                                            + ", idMaestro: " + request.idMaestro() + ", idAula: " + request.idAula()
                                            + " y periodo: " + request.periodo());

    }

    private void validarCambiosUnicos(GrupoRequest request, Long id){

        log.info("Validando clave única de grupo...");
        if (grupoRepository.existsByCursoIdAndMaestroIdAndAulaIdAndPeriodoAndIdNot(
                request.idCurso(),request.idMaestro(),request.idAula(),request.periodo(), id
            )
        )
            throw new IllegalArgumentException("Ya existe un grupo registrado con el idCurso: " + request.idCurso()
                    + ", idMaestro: " + request.idMaestro() + ", idAula: " + request.idAula() + " y periodo: " + request.periodo());

    }
}
