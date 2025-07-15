package com.isai.demowebregistrationsystem.controllers.login;

import com.isai.demowebregistrationsystem.model.dtos.registroInicioSesion.RegistroApoderadoDTO;
import com.isai.demowebregistrationsystem.model.dtos.registroInicioSesion.RegistroDocenteDTO;
import com.isai.demowebregistrationsystem.model.dtos.registroInicioSesion.RegistroEstudianteDTO;
import com.isai.demowebregistrationsystem.model.dtos.registroInicioSesion.RegistroUsuarioDTO;
import com.isai.demowebregistrationsystem.model.enums.Rol;
import com.isai.demowebregistrationsystem.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Usuario o contraseña incorrectos.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Has cerrado sesión exitosamente.");
        }
        return "auth/login";
    }


    @GetMapping("/registro")
    public String showRegistroSelectionForm(Model model) {
        model.addAttribute("roles", Arrays.asList(Rol.DOCENTE, Rol.ESTUDIANTE, Rol.APODERADO));
        return "auth/registro_seleccion_rol";
    }

    @GetMapping("/registro/form/{rol}")
    public String showSpecificRegistrationForm(@PathVariable String rol, Model model, RedirectAttributes redirectAttributes) {
        try {
            Rol selectedRol = Rol.valueOf(rol.toUpperCase());
            RegistroUsuarioDTO registroDTO; // Usamos el DTO base

            switch (selectedRol) {
                case DOCENTE:
                    registroDTO = new RegistroDocenteDTO();
                    break;
                case ESTUDIANTE:
                    registroDTO = new RegistroEstudianteDTO();
                    break;
                case APODERADO:
                    registroDTO = new RegistroApoderadoDTO();
                    break;
                default:
                    redirectAttributes.addFlashAttribute("errorMessage", "Rol de registro no válido.");
                    return "redirect:/auth/registro";
            }

            registroDTO.setRol(selectedRol);

            model.addAttribute("registroDTO", registroDTO);
            model.addAttribute("selectedRol", selectedRol);

            model.addAttribute("generos", Arrays.asList("Masculino", "Femenino"));
            model.addAttribute("tiposDocumento", Arrays.asList("DNI", "Pasaporte", "Carnet Extranjería", "Cédula de Identidad", "Licencia de Conducir"));
            model.addAttribute("estadosCivil", Arrays.asList("Soltero/a", "Casado/a", "Divorciado/a", "Viudo/a"));

            return "auth/registro_form_dinamico";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Rol de registro no válido.");
            return "redirect:/auth/registro";
        }
    }


    @PostMapping("/registro/guardar/admin")
    public String registrarAdmin(@ModelAttribute("registroDTO") @Valid RegistroUsuarioDTO registroDTO,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        registroDTO.setRol(Rol.ADMIN);
        return handleRegistration(registroDTO, result, redirectAttributes, model);
    }

    @PostMapping("/registro/guardar/docente")
    public String registrarDocente(@ModelAttribute("registroDTO") @Valid RegistroDocenteDTO registroDTO,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        registroDTO.setRol(Rol.DOCENTE);
        return handleRegistration(registroDTO, result, redirectAttributes, model);
    }

    @PostMapping("/registro/guardar/estudiante")
    public String registrarEstudiante(@ModelAttribute("registroDTO") @Valid RegistroEstudianteDTO registroDTO,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        registroDTO.setRol(Rol.ESTUDIANTE);
        return handleRegistration(registroDTO, result, redirectAttributes, model);
    }

    @PostMapping("/registro/guardar/apoderado")
    public String registrarApoderado(@ModelAttribute("registroDTO") @Valid RegistroApoderadoDTO registroDTO,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        registroDTO.setRol(Rol.APODERADO);
        return handleRegistration(registroDTO, result, redirectAttributes, model);
    }

    private String handleRegistration(RegistroUsuarioDTO registroDTO,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        if (!registroDTO.getPassword().equals(registroDTO.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.registroDTO", "Las contraseñas no coinciden.");
        }

        if (result.hasErrors()) {
            model.addAttribute("selectedRol", registroDTO.getRol());
            model.addAttribute("generos", Arrays.asList("Masculino", "Femenino"));
            model.addAttribute("tiposDocumento", Arrays.asList("DNI", "Pasaporte", "Carnet Extranjería", "Cédula de Identidad", "Licencia de Conducir"));
            model.addAttribute("estadosCivil", Arrays.asList("Soltero/a", "Casado/a", "Divorciado/a", "Viudo/a"));
            model.addAttribute("registroDTO", registroDTO);
            return "auth/registro_form_dinamico";
        }

        try {
            usuarioService.registrarNuevoUsuario(registroDTO);
            redirectAttributes.addFlashAttribute("successMessage", "¡Registro exitoso! Ya puedes iniciar sesión.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("nombre de usuario")) {
                result.rejectValue("username", "error.registroDTO", e.getMessage());
            } else if (e.getMessage().contains("DNI")) {
                result.rejectValue("dni", "error.registroDTO", e.getMessage());
            } else {
                model.addAttribute("errorMessage", e.getMessage());
            }
            model.addAttribute("selectedRol", registroDTO.getRol());
            model.addAttribute("generos", Arrays.asList("Masculino", "Femenino"));
            model.addAttribute("tiposDocumento", Arrays.asList("DNI", "Pasaporte", "Carnet Extranjería", "Cédula de Identidad", "Licencia de Conducir"));
            model.addAttribute("estadosCivil", Arrays.asList("Soltero/a", "Casado/a", "Divorciado/a", "Viudo/a"));
            model.addAttribute("registroDTO", registroDTO);
            return "auth/registro_form_dinamico";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al registrar: " + e.getMessage());
            return "redirect:/auth/registro";
        }
    }
}