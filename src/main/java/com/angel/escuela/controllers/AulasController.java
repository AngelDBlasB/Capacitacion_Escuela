package com.angel.escuela.controllers;

import com.angel.escuela.dto.aulas.AulaRequest;
import com.angel.escuela.dto.aulas.AulaResponse;
import com.angel.escuela.services.aulas.AulaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aulas")
public class AulasController extends CommonController<AulaRequest, AulaResponse, AulaService> {

    public AulasController(AulaService service) {
        super(service);
    }

}
