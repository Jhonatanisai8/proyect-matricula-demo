package com.isai.demowebregistrationsystem.controllers.publics;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/index")
@Controller
public class IndexController {

    @GetMapping()
    public String indexController() {
        return "index";
    }
}
