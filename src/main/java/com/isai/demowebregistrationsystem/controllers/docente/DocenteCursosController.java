package com.isai.demowebregistrationsystem.controllers.docente;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.dtos.docente.AsignacionDocenteDetalleDTO;
import com.isai.demowebregistrationsystem.model.dtos.docente.CursoAsignadoDTO;
import com.isai.demowebregistrationsystem.services.DocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(path = "/docente")
@RequiredArgsConstructor
public class DocenteCursosController {


    private final DocenteService docenteService;

    @GetMapping("/mis_cursos")
    public String listarMisCursos(Model model,
                                  RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            List<CursoAsignadoDTO> cursosAsignados = docenteService.listarCursosAsignadosPorUserName(username);
            model.addAttribute("cursosAsignados", cursosAsignados);
            return "docente/mis_cursos";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cargar tus cursos: " + e.getMessage());
            return "redirect:/docente/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al cargar tus cursos: " + e.getMessage());
            return "redirect:/docente/dashboard";
        }
    }

    @GetMapping("/mis_cursos/{idAsignacion}/detalles")
    public String verDetallesAsignacion(@PathVariable("idAsignacion") Integer idAsignacion,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            AsignacionDocenteDetalleDTO asignacionDetalle = docenteService.obtenerDetallesAsignacion(idAsignacion, username);
            model.addAttribute("asignacion", asignacionDetalle);

            return "docente/detalles_asignacion";
        } catch (ResourceNotFoundException | AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/docente/mis_cursos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al cargar los detalles de la asignación: " + e.getMessage());
            return "redirect:/docente/mis_cursos";
        }
    }
}
