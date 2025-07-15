package com.isai.demowebregistrationsystem.controllers.admin;

import com.isai.demowebregistrationsystem.model.dtos.SalonDTO;
import com.isai.demowebregistrationsystem.services.impl.SalonServiceImpl;
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

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/admin/salones")
public class AdminSalonController {


    private final SalonServiceImpl salonService;

    @GetMapping
    public String listarSalones(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SalonDTO> salonesPage = salonService.obtenerSalones(pageable);
        model.addAttribute("salones", salonesPage);
        return "admin/salones/lista_salones";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrearSalones(Model model) {
        model.addAttribute("salonDTO", new SalonDTO());
        model.addAttribute("isEdit", false);
        return "admin/salones/crear_salon";
    }

    @PostMapping("/guardar")
    public String guardarSalon(@Valid @ModelAttribute("salonDTO") SalonDTO salonDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", salonDTO.getIdSalon() != null && salonDTO.getIdSalon() != 0);
            return "admin/salones/crear_salon";
        }

        try {
            salonService.guardarSalon(salonDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    salonDTO.getIdSalon() == null || salonDTO.getIdSalon() == 0 ? "Salón registrado exitosamente." : "Salón actualizado exitosamente.");
            return "redirect:/admin/salones";
        } catch (IllegalArgumentException e) {
            result.rejectValue("codigoSalon", "error.salonDTO", e.getMessage()); // Bind error to specific field
            model.addAttribute("isEdit", salonDTO.getIdSalon() != null && salonDTO.getIdSalon() != 0);
            return "admin/salones/crear_salon";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar el salón: " + e.getMessage());
            return "redirect:/admin/salones";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarSalon(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            salonService.eliminarSalon(id);
            redirectAttributes.addFlashAttribute("successMessage", "Salón eliminado exitosamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el salón: " + e.getMessage());
        }
        return "redirect:/admin/salones";
    }

    @GetMapping("/detalle/{id}")
    public String mostrarDetallesSalon(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<SalonDTO> salonOptional = salonService.obtenerSalonPorId(id);
        if (salonOptional.isPresent()) {
            model.addAttribute("salon", salonOptional.get());
            return "admin/salones/detalle_salon";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Salón no encontrado.");
            return "redirect:/admin/salones";
        }
    }

    @GetMapping("/toggleActivo/{id}")
    public String alternarEstadoDelSalón(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            salonService.alternarEstadoDelSalón(id);
            redirectAttributes.addFlashAttribute("successMessage", "Estado del salón actualizado.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cambiar el estado del salón: " + e.getMessage());
        }
        return "redirect:/admin/salones";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("salonDTO", salonService.obtenerSalonPorId(id));
        model.addAttribute("isEdit", id != null);

        return "admin/salones/crear_salon";
    }
}
