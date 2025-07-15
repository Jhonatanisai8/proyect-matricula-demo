package com.isai.demowebregistrationsystem.controllers.admin;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.dtos.secciones.*;
import com.isai.demowebregistrationsystem.services.SeccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/admin/secciones")
public class AdminSeccionController {

    private final SeccionService seccionService;

    @GetMapping
    public String listarSecciones(Model model) {
        model.addAttribute("secciones", seccionService.listarTodasLasSecciones());
        return "admin/secciones/lista_secciones";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("seccionRegistroDTO", new SeccionRegistroDTO());
        model.addAttribute("docentes", seccionService.listarTodosDocentes());
        model.addAttribute("grados", seccionService.listarTodosGrados());
        model.addAttribute("periodosAcademicos", seccionService.listarTodosPeriodosAcademicos());
        model.addAttribute("isEdit", false); // Para diferenciar en la vista
        return "admin/secciones/crear_seccion";
    }

    @PostMapping("/crear")
    public String crearSeccion(@Valid @ModelAttribute("seccionRegistroDTO") SeccionRegistroDTO seccionRegistroDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            // Recargar datos para los dropdowns si hay errores
            model.addAttribute("docentes", seccionService.listarTodosDocentes());
            model.addAttribute("grados", seccionService.listarTodosGrados());
            model.addAttribute("periodosAcademicos", seccionService.listarTodosPeriodosAcademicos());
            model.addAttribute("isEdit", false);
            return "admin/secciones/crear_seccion";
        }

        try {
            seccionService.guardarSeccion(seccionRegistroDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Sección creada exitosamente.");
            return "redirect:/admin/secciones";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al crear sección: " + e.getMessage());
            return "redirect:/admin/secciones/crear";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al crear sección: " + e.getMessage());
            return "redirect:/admin/secciones/crear";
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            SeccionDetalleDTO seccionDetalleDTO = seccionService.obtenerSeccionPorId(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Sección no encontrada con ID: " + id));

            // convertimos SeccionDetalleDTO a SeccionRegistroDTO para el formulario
            SeccionRegistroDTO seccionRegistroDTO = new SeccionRegistroDTO(
                    seccionDetalleDTO.getIdSeccion(),
                    seccionDetalleDTO.getNombreSeccion(),
                    seccionDetalleDTO.getIdDocenteTutor(),
                    seccionDetalleDTO.getIdGrado(),
                    seccionDetalleDTO.getIdPeriodoAcademico()
            );

            model.addAttribute("seccionRegistroDTO", seccionRegistroDTO);
            model.addAttribute("docentes", seccionService.listarTodosDocentes());
            model.addAttribute("grados", seccionService.listarTodosGrados());
            model.addAttribute("periodosAcademicos", seccionService.listarTodosPeriodosAcademicos());
            model.addAttribute("isEdit", true); // Para diferenciar en la vista
            return "admin/secciones/crear_seccion";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/secciones";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarSeccion(@PathVariable Long id,
                                @Valid @ModelAttribute("seccionRegistroDTO") SeccionRegistroDTO seccionRegistroDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        // Asegurarse de que el ID del DTO coincida con el ID de la URL
        if (!id.equals(seccionRegistroDTO.getIdSeccion())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: ID de la sección no coincide.");
            return "redirect:/admin/secciones";
        }

        if (result.hasErrors()) {
            // Recargar datos para los dropdowns si hay errores
            model.addAttribute("docentes", seccionService.listarTodosDocentes());
            model.addAttribute("grados", seccionService.listarTodosGrados());
            model.addAttribute("periodosAcademicos", seccionService.listarTodosPeriodosAcademicos());
            model.addAttribute("isEdit", true);
            return "admin/secciones/crear_seccion";
        }

        try {
            seccionService.guardarSeccion(seccionRegistroDTO); // Reutilizamos el mismo método para guardar y actualizar
            redirectAttributes.addFlashAttribute("successMessage", "Sección actualizada exitosamente.");
            return "redirect:/admin/secciones";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar sección: " + e.getMessage());
            return "redirect:/admin/secciones/editar/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al actualizar sección: " + e.getMessage());
            return "redirect:/admin/secciones/editar/" + id;
        }
    }

    @GetMapping("/{id}")
    public String verDetalleSeccion(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            SeccionDetalleDTO seccion = seccionService.obtenerSeccionPorId(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Sección no encontrada con ID: " + id));
            model.addAttribute("seccion", seccion);
            return "admin/secciones/detalle_seccion";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/secciones";
        }
    }

    // --- Eliminar una sección ---
    @PostMapping("/eliminar/{id}")
    public String eliminarSeccion(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            seccionService.eliminarSeccion(id);
            redirectAttributes.addFlashAttribute("successMessage", "Sección eliminada exitosamente.");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar sección: " + e.getMessage());
        }
        return "redirect:/admin/secciones";
    }

}
