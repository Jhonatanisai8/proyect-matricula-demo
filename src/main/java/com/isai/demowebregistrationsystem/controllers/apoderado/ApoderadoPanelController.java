package com.isai.demowebregistrationsystem.controllers.apoderado;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.dtos.apoderado.DashboardApoderadpDTO;
import com.isai.demowebregistrationsystem.services.ApoderadoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping(path = "/apoderado/dashboard")
public class ApoderadoPanelController {

    private final ApoderadoService apoderadoService;

    public ApoderadoPanelController(ApoderadoService apoderadoService) {
        this.apoderadoService = apoderadoService;
    }

    @GetMapping("")
    public String showApoderadoDashboard(
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            String username = principal.getName();
            DashboardApoderadpDTO dashboardData = apoderadoService.obtenerDatosDashboardApoderado(username);
            model.addAttribute("dashboardData", dashboardData);
            return "apoderado/dashboard-apoderado";
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
