package com.isai.demowebregistrationsystem.controllers.estudiante;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante.CursosEstudianteViewDTO;
import com.isai.demowebregistrationsystem.services.EstudianteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.*;

import java.security.Principal;

@Controller
@RequestMapping(path = "/estudiante")
public class EstudianteCursosController {
    private final EstudianteService estudianteService;

    public EstudianteCursosController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping("/mis_cursos")
    public String showMisCursos(
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            String username = principal.getName();
            CursosEstudianteViewDTO cursosData = estudianteService.obtenerMisCursos(username);
            model.addAttribute("cursosData", cursosData);
            return "estudiante/mis_cursos";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/estudiante/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error interno del servidor al cargar tus cursos.");
            return "redirect:/estudiante/dashboard";
        }
    }
}
