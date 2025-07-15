package com.isai.demowebregistrationsystem.controllers.admin;

import com.isai.demowebregistrationsystem.model.dtos.UsuarioDTO;
import com.isai.demowebregistrationsystem.model.enums.Rol; // Asegúrate de que tu Enum se llame 'Rol' y esté en 'enums'
import com.isai.demowebregistrationsystem.services.UsuarioService; // Usar la interfaz, no la implementación directa
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public String listUsuarios(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioDTO> usuariosPage = usuarioService.obtenerUsuarios(pageable);
        model.addAttribute("usuarios", usuariosPage);
        return "admin/usuarios/lista_usuarios";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrearUsuario(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        model.addAttribute("isEdit", false);
        model.addAttribute("roles", Arrays.asList(Rol.values()));
        model.addAttribute("personasDisponibles", usuarioService.encontrarPersonasDisponibles()); // Correcto
        return "admin/usuarios/crear_usuario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<UsuarioDTO> usuarioOptional = usuarioService.obtenerUsuarioPorId(id);
        if (usuarioOptional.isPresent()) {
            model.addAttribute("usuarioDTO", usuarioOptional.get());
            model.addAttribute("isEdit", true);
            model.addAttribute("roles", Arrays.asList(Rol.values()));
            // CAMBIO CRÍTICO AQUÍ: Usar encontrarPersonasDisponiblesParaUsuario
            model.addAttribute("personasDisponibles", usuarioService.encontrarPersonasDisponiblesParaUsuario(id));
            return "admin/usuarios/crear_usuario";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Usuario no encontrado.");
            return "redirect:/admin/usuarios";
        }
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            if (!usuarioDTO.getPassword().equals(usuarioDTO.getConfirmPassword())) {
                result.rejectValue("confirmPassword", "password.mismatch", "Las contraseñas no coinciden.");
            }
        } else if (usuarioDTO.getIdUsuario() == null && (usuarioDTO.getPassword() == null || usuarioDTO.getPassword().isEmpty())) {
            result.rejectValue("password", "password.required", "La contraseña es obligatoria para un nuevo usuario.");
        }


        if (result.hasErrors()) {
            model.addAttribute("isEdit", usuarioDTO.getIdUsuario() != null && usuarioDTO.getIdUsuario() != 0);
            model.addAttribute("roles", Arrays.asList(Rol.values()));
            // CAMBIO CRÍTICO AQUÍ: Usar encontrarPersonasDisponiblesParaUsuario
            model.addAttribute("personasDisponibles", usuarioService.encontrarPersonasDisponiblesParaUsuario(usuarioDTO.getIdUsuario()));
            return "admin/usuarios/crear_usuario";
        }

        try {
            usuarioService.guardarUsuario(usuarioDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    usuarioDTO.getIdUsuario() == null || usuarioDTO.getIdUsuario() == 0 ? "Usuario registrado exitosamente." : "Usuario actualizado exitosamente.");
            return "redirect:/admin/usuarios";
        } catch (IllegalArgumentException e) {

            if (e.getMessage().contains("nombre de usuario")) {
                result.rejectValue("userName", "error.usuarioDTO", e.getMessage());
            } else if (e.getMessage().contains("persona")) {
                result.rejectValue("personaId", "error.usuarioDTO", e.getMessage());
            } else if (e.getMessage().contains("contraseña")) {
                result.rejectValue("password", "error.usuarioDTO", e.getMessage());
            } else {
                result.rejectValue("idUsuario", "error.usuarioDTO", e.getMessage());
            }

            model.addAttribute("isEdit", usuarioDTO.getIdUsuario() != null && usuarioDTO.getIdUsuario() != 0);
            model.addAttribute("roles", Arrays.asList(Rol.values()));
            // CAMBIO CRÍTICO AQUÍ: Usar encontrarPersonasDisponiblesParaUsuario
            model.addAttribute("personasDisponibles", usuarioService.encontrarPersonasDisponiblesParaUsuario(usuarioDTO.getIdUsuario()));
            return "admin/usuarios/crear_usuario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar el usuario: " + e.getMessage());
            return "redirect:/admin/usuarios";
        }
    }

    @GetMapping("/detalle/{id}")
    public String viewUserDetails(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<UsuarioDTO> usuarioOptional = usuarioService.obtenerUsuarioPorId(id);
        if (usuarioOptional.isPresent()) {
            model.addAttribute("usuario", usuarioOptional.get());
            return "admin/usuarios/detalle_usuario";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Usuario no encontrado.");
            return "redirect:/admin/usuarios";
        }
    }

    @GetMapping("/toggleActivo/{id}")
    public String toggleActiveStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.alternarEstadoUsuario(id);
            redirectAttributes.addFlashAttribute("successMessage", "Estado del usuario actualizado.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cambiar el estado del usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("asd asd @1"));
    }
}