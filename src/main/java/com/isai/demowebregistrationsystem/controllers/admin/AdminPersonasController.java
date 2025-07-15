package com.isai.demowebregistrationsystem.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/admin/personas")
public class AdminPersonasController {

    @GetMapping(path = "")
    public String mostrarPersonas() {
        return "admin/lista-personas";
    }
}
