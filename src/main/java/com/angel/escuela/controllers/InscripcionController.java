package com.angel.escuela.controllers;

import com.angel.escuela.dto.inscripciones.InscripcionRequest;
import com.angel.escuela.dto.inscripciones.InscripcionResponse;
import com.angel.escuela.services.inscripciones.InscripcionesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController extends CommonController<InscripcionRequest, InscripcionResponse, InscripcionesService> {

    public InscripcionController(InscripcionesService service) {
        super(service);
    }
}
