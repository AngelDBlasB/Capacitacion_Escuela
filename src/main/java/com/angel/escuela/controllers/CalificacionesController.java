package com.angel.escuela.controllers;

import com.angel.escuela.dto.calificaciones.CalificacionRequest;
import com.angel.escuela.dto.calificaciones.CalificacionResponse;
import com.angel.escuela.services.calificaciones.CalificacionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionesController extends CommonController<CalificacionRequest, CalificacionResponse, CalificacionService> {
    public CalificacionesController(CalificacionService service) {
        super(service);
    }
}
