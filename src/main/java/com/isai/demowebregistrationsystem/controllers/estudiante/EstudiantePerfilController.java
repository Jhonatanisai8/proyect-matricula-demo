package com.isai.demowebregistrationsystem.controllers.estudiante;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.EstudianteDetalleDTO;
import com.isai.demowebregistrationsystem.model.entities.Estudiante;
import com.isai.demowebregistrationsystem.model.entities.Usuario;
import com.isai.demowebregistrationsystem.repositorys.EstudianteRepository;
import com.isai.demowebregistrationsystem.repositorys.UsuarioRepository;
import com.isai.demowebregistrationsystem.services.EstudianteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;


@Controller
@RequestMapping(path = "/estudiante")
public class EstudiantePerfilController {

    private final EstudianteService estudianteService;
    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;

    public EstudiantePerfilController(EstudianteService estudianteService, UsuarioRepository usuarioRepository, EstudianteRepository estudianteRepository) {
        this.estudianteService = estudianteService;
        this.usuarioRepository = usuarioRepository;
        this.estudianteRepository = estudianteRepository;
    }

    @GetMapping("/perfil")
    public String verDetalleEstudiante(
            Model model,
            Principal principal,
            @RequestParam(name = "success", required = false) String successMessage,
            @RequestParam(name = "error", required = false) String errorMessage,
            RedirectAttributes redirectAttributes) {
        try {
            String username = principal.getName();
            Usuario usuario = usuarioRepository.findByUserName(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));
            Estudiante estudiante = estudianteRepository.findByPersonaIdPersona(usuario.getPersona().getIdPersona())
                    .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado para el usuario: " + username));
            Integer id = estudiante.getIdEstudiante();
            EstudianteDetalleDTO estudiante1 = estudianteService.obtenerEstudianteDetalle(id);
            model.addAttribute("estudiante", estudiante1);
            if (successMessage != null) {
                model.addAttribute("successMessage", successMessage);
            }
            if (errorMessage != null) {
                model.addAttribute("errorMessage", errorMessage);
            }
            return "estudiante/mi_perfil";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/estudiantes/lista";
        }
    }
}
