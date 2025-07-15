package com.isai.demowebregistrationsystem.controllers.apoderado;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.EstudianteRegistroDTO;
import com.isai.demowebregistrationsystem.services.ApoderadoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping(path = "/apoderado")
public class ApoderadoHijosController {

    private final ApoderadoService apoderadoService;

    public ApoderadoHijosController(ApoderadoService apoderadoService) {
        this.apoderadoService = apoderadoService;
    }

    @GetMapping("/registrar-hijo")
    public String showRegistrarHijoForm(Model model) {
        model.addAttribute("estudianteRegistroDTO", new EstudianteRegistroDTO());
        model.addAttribute("generos", new String[]{"Masculino", "Femenino"});
        model.addAttribute("tiposDocumento", new String[]{"DNI", "Carnet Extranjería", "Pasaporte"});
        model.addAttribute("estadoCivilOpciones", new String[]{"Soltero(a)"});
        return "apoderado/registrar_hijo";
    }

    @PostMapping("/registrar-hijo")
    public String registrarHijo(@Valid @ModelAttribute("estudianteRegistroDTO") EstudianteRegistroDTO estudianteRegistroDTO,
                                BindingResult bindingResult,
                                Principal principal,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("generos", new String[]{"Masculino", "Femenino"});
            model.addAttribute("tiposDocumento", new String[]{"DNI", "Carnet Extranjería", "Pasaporte"});
            model.addAttribute("estadoCivilOpciones", new String[]{"Soltero(a)"});
            return "apoderado/registrar_hijo";
        }

        try {
            String usernameApoderado = principal.getName();
            apoderadoService.registrarNuevoEstudiante(estudianteRegistroDTO, usernameApoderado);
            redirectAttributes.addFlashAttribute("successMessage", "¡Estudiante registrado y vinculado exitosamente!");
            return "redirect:/apoderado/dashboard";
        } catch (IllegalArgumentException e) {

            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("generos", new String[]{"Masculino", "Femenino"});
            model.addAttribute("tiposDocumento", new String[]{"DNI", "Carnet Extranjería", "Pasaporte"});
            model.addAttribute("estadoCivilOpciones", new String[]{"Soltero(a)"});
            return "apoderado/registrar_hijo";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al encontrar el apoderado: " + e.getMessage());
            return "redirect:/logout";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error interno al registrar al estudiante.");
            return "redirect:/apoderado/dashboard";
        }
    }
}
