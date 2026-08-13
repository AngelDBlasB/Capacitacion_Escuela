package com.angel.escuela.controllers;

import com.angel.escuela.dto.cursos.CursoRequest;
import com.angel.escuela.dto.cursos.CursoResponse;
import com.angel.escuela.services.cursos.CursoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cursos")
public class CursoController extends CommonController<CursoRequest, CursoResponse, CursoService>{

    public CursoController(CursoService service) {
        super(service);
    }

}
