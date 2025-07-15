package com.isai.demowebregistrationsystem.controllers.estudiante;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.constantes.HorarioConstantes;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante.HorarioEstudianteViewDTO;
import com.isai.demowebregistrationsystem.services.EstudianteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping(path = "/estudiante")
public class EstudianteHorarioController {
    private final EstudianteService estudianteService;

    public EstudianteHorarioController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping("/mi_horario")
    public String showMiHorario(Model model,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        try {
            String username = principal.getName();
            HorarioEstudianteViewDTO horarioData = estudianteService.obtenerMiHorario(username);
            model.addAttribute("horarioData", horarioData);

            // Agrega estas dos líneas
            model.addAttribute("horasDelDia", HorarioConstantes.HORAS_DEL_DIA);
            model.addAttribute("diasOrdenados", horarioData.getDiasOrdenados());

            return "estudiante/mi_horario";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/estudiante/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error interno del servidor al cargar tu horario.");
            return "redirect:/estudiante/dashboard";
        }
    }

}
