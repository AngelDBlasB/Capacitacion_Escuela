package com.angel.escuela.controllers;

import com.angel.escuela.dto.grupos.GrupoRequest;
import com.angel.escuela.dto.grupos.GrupoResponse;
import com.angel.escuela.entities.Grupo;
import com.angel.escuela.services.cursos.CursoService;
import com.angel.escuela.services.grupos.GruposService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController extends CommonController<GrupoRequest, GrupoResponse, GruposService>{

    public GrupoController(GruposService service) {
        super(service);
    }

}
