package com.isai.demowebregistrationsystem.controllers.docente;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.exceptions.ValidationException;
import com.isai.demowebregistrationsystem.model.dtos.docente.DocentePerfilDTO;
import com.isai.demowebregistrationsystem.services.impl.DocenteServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(path = "/docente")
@RequiredArgsConstructor
public class DocentePanelController {

    private final DocenteServiceImpl docenteService;


    @GetMapping("/dashboard")
    public String docenteDashboard(Model model) {
        // Aquí podrías cargar datos específicos del dashboard del docente
        // Por ejemplo, los próximos horarios, cursos asignados, etc.
        // Por ahora, solo un mensaje de bienvenida.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        return "docente/dashboard";
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Obtener el username del usuario logueado

        try {
            DocentePerfilDTO docentePerfilDTO = docenteService.obtenerPerfilDocentePorUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado para el usuario: " + username));

            model.addAttribute("docentePerfilDTO", docentePerfilDTO);
            System.out.println(docentePerfilDTO.getTipoDocumento());
            cargarDatosSelect(model);
            return "docente/perfil";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cargar perfil: " + e.getMessage());
            return "redirect:/docente/dashboard"; // Redirigir al dashboard con error
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al cargar perfil: " + e.getMessage());
            return "redirect:/docente/dashboard";
        }
    }

    @PostMapping("/perfil")
    public String actualizarPerfil(@Valid @ModelAttribute("docentePerfilDTO") DocentePerfilDTO docentePerfilDTO,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        if (result.hasErrors()) {
            cargarDatosSelect(model);
            return "docente/perfil";
        }

        try {
            docenteService.actualizarPerfilDocente(docentePerfilDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Perfil actualizado exitosamente.");
            return "redirect:/docente/perfil";
        } catch (ValidationException e) {
            // Error de validación de negocio (ej. DNI duplicado)
            result.reject(null, e.getMessage()); // Añadir el error al BindingResult
            cargarDatosSelect(model);
            return "docente/perfil";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar perfil: " + e.getMessage());
            return "redirect:/docente/perfil";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al actualizar perfil: " + e.getMessage());
            return "redirect:/docente/perfil";
        }
    }

    private void cargarDatosSelect(Model model) {
        model.addAttribute("estadosCiviles", docenteService.getEstadosCiviles());
        model.addAttribute("generos", docenteService.getGeneros());
        model.addAttribute("tiposDocumento", docenteService.getTiposDocumento());
    }
}
