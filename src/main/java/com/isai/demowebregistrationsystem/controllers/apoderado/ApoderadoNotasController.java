package com.isai.demowebregistrationsystem.controllers.apoderado;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/apoderado/notas")
public class ApoderadoNotasController {

    @GetMapping("")
    public String mostrarApoderadoNotas() {
        return "apoderado/hijos_notas";
    }
}
