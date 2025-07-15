package com.isai.demowebregistrationsystem.controllers.admin;

import com.isai.demowebregistrationsystem.model.dtos.PeriodoAcademicoDTO;
import com.isai.demowebregistrationsystem.services.PeriodoAcademicoService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/periodos")
public class AdminPeriodoAcademicoController {

    private final PeriodoAcademicoService periodoAcademicoService;

    private final List<String> ESTADOS_PERIODO = Arrays.asList("PENDIENTE", "EN_CURSO", "FINALIZADO", "CANCELADO");

    @GetMapping
    public String listarPeriodos(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PeriodoAcademicoDTO> periodosPage = periodoAcademicoService.obtenerPeriodos(pageable);
        model.addAttribute("periodos", periodosPage);
        return "admin/periodos_academicos/lista_periodos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrearPeriodo(Model model) {
        PeriodoAcademicoDTO nuevoPeriodo = new PeriodoAcademicoDTO();
        nuevoPeriodo.setAnoAcademico(LocalDate.now().getYear()); // Sugiere el año actual
        model.addAttribute("periodoDTO", nuevoPeriodo);
        model.addAttribute("isEdit", false);
        model.addAttribute("estadosPeriodo", ESTADOS_PERIODO);
        return "admin/periodos_academicos/crear_periodo";
    }


    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarPeriodo(@PathVariable Integer id,
                                                 Model model,
                                                 RedirectAttributes redirectAttributes) {
        Optional<PeriodoAcademicoDTO> periodoOptional = periodoAcademicoService.obtenerPeriodoPorId(id);
        if (periodoOptional.isPresent()) {
            model.addAttribute("periodoDTO", periodoOptional.get());
            model.addAttribute("isEdit", true);
            model.addAttribute("estadosPeriodo", ESTADOS_PERIODO);
            return "admin/periodos_academicos/crear_periodo";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Período Académico no encontrado.");
            return "redirect:/admin/periodos";
        }
    }

    @PostMapping("/guardar")
    public String guardarPeriodo(@Valid @ModelAttribute("periodoDTO") PeriodoAcademicoDTO periodoDTO,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", periodoDTO.getIdPeriodo() != null && periodoDTO.getIdPeriodo() != 0);
            model.addAttribute("estadosPeriodo", ESTADOS_PERIODO);
            return "admin/periodos_academicos/crear_periodo";
        }

        try {
            periodoAcademicoService.guardarPeriodo(periodoDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    periodoDTO.getIdPeriodo() == null || periodoDTO.getIdPeriodo() == 0 ? "Período Académico registrado exitosamente." : "Período Académico actualizado exitosamente.");
            return "redirect:/admin/periodos";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("fecha")) {
                result.rejectValue("fechaFin", "error.periodoDTO", e.getMessage());
            } else if (e.getMessage().contains("período")) {
                result.rejectValue("nombrePeriodo", "error.periodoDTO", e.getMessage());
            } else {
                result.rejectValue("idPeriodo", "error.periodoDTO", e.getMessage());
            }

            model.addAttribute("isEdit", periodoDTO.getIdPeriodo() != null && periodoDTO.getIdPeriodo() != 0);
            model.addAttribute("estadosPeriodo", ESTADOS_PERIODO); // Recargar estados
            return "admin/periodos_academicos/crear_periodo";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar el período académico: " + e.getMessage());
            return "redirect:/admin/periodos";
        }
    }

    @GetMapping("/detalle/{id}")
    public String verDetallePeriodo(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<PeriodoAcademicoDTO> periodoOptional = periodoAcademicoService.obtenerPeriodoPorId(id);
        if (periodoOptional.isPresent()) {
            model.addAttribute("periodo", periodoOptional.get());
            return "admin/periodos_academicos/detalle_periodo"; // Ajusta la ruta de la vista
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Período Académico no encontrado.");
            return "redirect:/admin/periodos";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPeriodo(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            periodoAcademicoService.eliminarPeriodo(id);
            redirectAttributes.addFlashAttribute("successMessage", "Período Académico eliminado exitosamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el período académico: " + e.getMessage());
        }
        return "redirect:/admin/periodos";
    }

    @GetMapping("/toggleActivo/{id}")
    public String alternarEstadoPeriodo(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            periodoAcademicoService.alternarEstadoPeriodo(id);
            redirectAttributes.addFlashAttribute("successMessage", "Estado del período académico actualizado.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cambiar el estado del período académico: " + e.getMessage());
        }
        return "redirect:/admin/periodos";
    }
}
