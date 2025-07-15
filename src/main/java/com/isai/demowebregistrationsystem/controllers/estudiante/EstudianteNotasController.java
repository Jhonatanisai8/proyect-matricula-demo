package com.isai.demowebregistrationsystem.controllers.estudiante;


import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante.NotasEstudianteViewDTO;
import com.isai.demowebregistrationsystem.services.EstudianteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import java.security.Principal;

@Controller
@RequestMapping("/estudiante")
public class EstudianteNotasController {

    private final EstudianteService estudianteService;

    public EstudianteNotasController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping("/mis_notas")
    public String showMisNotas(
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            String username = principal.getName();
            NotasEstudianteViewDTO notasData = estudianteService.obtenerMisNotas(username);
            model.addAttribute("notasData", notasData);
            return "estudiante/mis_notas";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/estudiante/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error interno del servidor al cargar tus notas.");
            return "redirect:/estudiante/dashboard";
        }
    }
}
