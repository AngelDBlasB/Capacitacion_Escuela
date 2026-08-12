package com.angel.escuela.controllers;

import com.angel.escuela.dto.alumnos.AlumnoRequest;
import com.angel.escuela.dto.alumnos.AlumnoResponse;
import com.angel.escuela.services.alumnos.AlumnoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController extends CommonController<AlumnoRequest, AlumnoResponse, AlumnoService>{

    public AlumnoController(AlumnoService service) {
        super(service);
    }
}
