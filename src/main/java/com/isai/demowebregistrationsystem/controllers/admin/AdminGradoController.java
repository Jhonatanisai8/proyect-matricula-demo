package com.isai.demowebregistrationsystem.controllers.admin;

import com.isai.demowebregistrationsystem.model.dtos.GradoDTO;
import com.isai.demowebregistrationsystem.services.GradoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/admin/grados")
public class AdminGradoController {

    private final GradoService gradoService;

    @GetMapping
    public String listarGrados(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GradoDTO> gradosPage = gradoService.obtenerGrados(pageable);
        model.addAttribute("grados", gradosPage);
        return "admin/grados/lista_grados";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrearGrado(Model model) {
        model.addAttribute("gradoDTO", new GradoDTO());
        model.addAttribute("isEdit", false);
        return "admin/grados/crear_grado";
    }

    @PostMapping("/guardar")
    public String guardarGrado(@Valid @ModelAttribute("gradoDTO") GradoDTO gradoDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", gradoDTO.getIdGrado() != null && gradoDTO.getIdGrado() != 0);
            return "admin/grados/crear_grado";
        }

        try {
            gradoService.guardarGrado(gradoDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    gradoDTO.getIdGrado() == null || gradoDTO.getIdGrado() == 0 ? "Grado registrado exitosamente." : "Grado actualizado exitosamente.");
            return "redirect:/admin/grados";
        } catch (IllegalArgumentException e) {
            // Error de negocio (ej. código duplicado)
            result.rejectValue("codigoGrado", "error.gradoDTO", e.getMessage());
            if (e.getMessage().contains("nombre")) {
                result.rejectValue("nombreGrado", "error.gradoDTO", e.getMessage());
            }

            model.addAttribute("isEdit", gradoDTO.getIdGrado() != null && gradoDTO.getIdGrado() != 0);
            return "admin/grados/crear_grado";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar el grado: " + e.getMessage());
            return "redirect:/admin/grados";
        }
    }


    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarGrado(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<GradoDTO> gradoOptional = gradoService.obtenerGradoPorId(id);
        if (gradoOptional.isPresent()) {
            model.addAttribute("gradoDTO", gradoOptional.get());
            model.addAttribute("isEdit", true);
            return "admin/grados/crear_grado";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Grado no encontrado.");
            return "redirect:/admin/grados";
        }
    }

    @GetMapping("/detalle/{id}")
    public String verDetalleGrado(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<GradoDTO> gradoOptional = gradoService.obtenerGradoPorId(id);
        if (gradoOptional.isPresent()) {
            model.addAttribute("grado", gradoOptional.get());
            return "admin/grados/detalle_grado";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Grado no encontrado.");
            return "redirect:/admin/grados";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarGrado(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            gradoService.eliminarGrado(id);
            redirectAttributes.addFlashAttribute("successMessage", "Grado eliminado exitosamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el grado: " + e.getMessage());
        }
        return "redirect:/admin/grados";
    }

    @GetMapping("/toggleActivo/{id}")
    public String alternarEstadoGrado(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            gradoService.alternarEstadoGrado(id);
            redirectAttributes.addFlashAttribute("successMessage", "Estado del grado actualizado.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cambiar el estado del grado: " + e.getMessage());
        }
        return "redirect:/admin/grados";
    }
}
