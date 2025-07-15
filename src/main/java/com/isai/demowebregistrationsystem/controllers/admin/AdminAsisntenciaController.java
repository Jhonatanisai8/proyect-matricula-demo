package com.isai.demowebregistrationsystem.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/admin/asistencia")
public class AdminAsisntenciaController {

    @GetMapping(path = "")
    public String asistencia() {
        return "admin/asistencia/asistencia";
    }
}
