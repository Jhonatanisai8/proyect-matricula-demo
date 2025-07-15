package com.isai.demowebregistrationsystem.controllers.estudiante;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/estudiante/asistencias")
public class EstudianteAsistenciasController {

    @GetMapping(path = "")
    public String mostrarEstudianteAsistencias() {
        return "estudiante/mis_asistencias";
    }
}
