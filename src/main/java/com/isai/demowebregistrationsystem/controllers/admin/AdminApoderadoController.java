package com.isai.demowebregistrationsystem.controllers.admin;


import com.isai.demowebregistrationsystem.model.dtos.ApoderadoDTO;
import com.isai.demowebregistrationsystem.services.impl.ApoderadoServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/apoderados")
public class AdminApoderadoController {

    private final ApoderadoServiceImpl apoderadoService;

    @RequestMapping(path = "")
    public String listarApoderados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String dni,
            Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("persona.apellidos").ascending());
        Page<ApoderadoDTO> apoderadosPage = apoderadoService.buscarApoderados(dni, pageable);
        model.addAttribute("apoderadosPage", apoderadosPage);
        model.addAttribute("dni", dni);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", apoderadosPage.getTotalPages());
        model.addAttribute("totalItems", apoderadosPage.getTotalElements());
        model.addAttribute("title", "Gestión de Apoderados");
        return "admin/apoderados/lista_apoderados";
    }

    @RequestMapping(path = "nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("apoderadoDTO", new ApoderadoDTO());
        model.addAttribute("title", "Registrar Nuevo Apoderado");
        model.addAttribute("isEdit", false);
        return "admin/apoderados/crear_apoderado";
    }

    @RequestMapping(path = "/guardar", method = RequestMethod.POST)
    public String guardarApoderado(@Valid @ModelAttribute("apoderadoDTO") ApoderadoDTO apoderadoDTO,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        if (apoderadoDTO.isPasswordRequired() || (apoderadoDTO.getPassword() != null && !apoderadoDTO.getPassword().isEmpty())) {
            if (apoderadoDTO.getPassword() == null || apoderadoDTO.getPassword().isEmpty()) {
                result.rejectValue("password", "field.notblank", "La contraseña no puede estar vacía.");
            } else if (apoderadoDTO.getPassword().length() < 6) {
                result.rejectValue("password", "size.password", "La contraseña debe tener al menos 6 caracteres.");
            }
            if (!apoderadoDTO.getPassword().equals(apoderadoDTO.getConfirmPassword())) {
                result.rejectValue("confirmPassword", "password.mismatch", "Las contraseñas no coinciden.");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("title", apoderadoDTO.getIdApoderado() == null ? "Registrar Nuevo Apoderado" : "Editar Apoderado");
            model.addAttribute("isEdit", apoderadoDTO.getIdApoderado() != null);
            return "admin/apoderados/crear_apoderado";
        }

        try {
            apoderadoService.guardarApoderado(apoderadoDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Apoderado y cuenta de usuario guardados exitosamente!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("title", apoderadoDTO.getIdApoderado() == null ? "Registrar Nuevo Apoderado" : "Editar Apoderado");
            model.addAttribute("isEdit", apoderadoDTO.getIdApoderado() != null);
            return "admin/apoderados/crear_apoderado";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("title", apoderadoDTO.getIdApoderado() == null ? "Registrar Nuevo Apoderado" : "Editar Apoderado");
            model.addAttribute("isEdit", apoderadoDTO.getIdApoderado() != null);
            return "admin/apoderados/crear_apoderado";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error inesperado al guardar el apoderado: " + e.getMessage());
            model.addAttribute("title", apoderadoDTO.getIdApoderado() == null ? "Registrar Nuevo Apoderado" : "Editar Apoderado");
            model.addAttribute("isEdit", apoderadoDTO.getIdApoderado() != null);
            return "admin/apoderados/crear_apoderado";
        }
        return "redirect:/admin/apoderados";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        return apoderadoService.buscarApoderadoPorId(id).map(apoderadoDTO -> {
            model.addAttribute("apoderadoDTO", apoderadoDTO);
            model.addAttribute("title", "Editar Apoderado");
            model.addAttribute("isEdit", true);
            apoderadoDTO.setPassword(null);
            apoderadoDTO.setConfirmPassword(null);
            return "admin/apoderados/crear_apoderado";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Apoderado no encontrado para editar.");
            return "redirect:/admin/apoderados";
        });
    }

    @GetMapping("/detalle/{id}")
    public String verDetalleApoderado(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        return apoderadoService.buscarApoderadoPorId(id).map(apoderadoDTO -> {
            model.addAttribute("apoderado", apoderadoDTO);
            model.addAttribute("title", "Detalle de Apoderado");
            System.out.println(apoderadoDTO.getRutaImagen());
            return "admin/apoderados/detalle_apoderado";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Apoderado no encontrado para ver detalle.");
            return "redirect:/admin/apoderados";
        });
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarApoderado(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            apoderadoService.eliminarApoderado(id);
            redirectAttributes.addFlashAttribute("successMessage", "Apoderado, persona y cuenta de usuario eliminados exitosamente.");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el apoderado: " + e.getMessage());
        }
        return "redirect:/admin/apoderados";
    }
}