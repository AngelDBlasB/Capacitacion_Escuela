package com.angel.escuela.controllers;

import com.angel.escuela.dto.horarios.HorarioRequest;
import com.angel.escuela.dto.horarios.HorarioResponse;
import com.angel.escuela.services.horarios.HorarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController extends CommonController<HorarioRequest, HorarioResponse, HorarioService>{
    public HorarioController(HorarioService service) {
        super(service);
    }
}
