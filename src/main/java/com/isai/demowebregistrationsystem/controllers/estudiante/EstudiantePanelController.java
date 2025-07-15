package com.isai.demowebregistrationsystem.controllers.estudiante;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante.EstudianteDashboardDTO;
import com.isai.demowebregistrationsystem.services.EstudianteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping(path = "/estudiante")
public class EstudiantePanelController {

    private final EstudianteService estudianteService;

    public EstudiantePanelController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping("/dashboard")
    public String showEstudianteDashboard(
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            String username = principal.getName();
            EstudianteDashboardDTO dashboardData = estudianteService.obtenerDatosDashboardEstudiante(username);
            model.addAttribute("dashboardData", dashboardData);
            return "estudiante/dashboard";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/logout";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error interno del servidor al cargar el dashboard.");
            return "redirect:/logout";
        }
    }

}
