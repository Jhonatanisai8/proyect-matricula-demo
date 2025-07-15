package com.isai.demowebregistrationsystem.controllers.admin;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.exceptions.ValidationException;
import com.isai.demowebregistrationsystem.model.dtos.MatriculaDTO;
import com.isai.demowebregistrationsystem.model.dtos.MatriculaDetalleDTO;
import com.isai.demowebregistrationsystem.services.MatriculaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping(path = "/admin/matricula")
public class AdminMatriculaController {

    private final MatriculaService matriculaService;

    public AdminMatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    @GetMapping
    public String listarMatriculas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaMatricula") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search,
            Model model) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<MatriculaDTO> matriculasPage = matriculaService.listarTodasMatriculas(search, pageable);

        model.addAttribute("matriculasPage", matriculasPage);
        model.addAttribute("currentPage", matriculasPage.getNumber());
        model.addAttribute("totalPages", matriculasPage.getTotalPages());
        model.addAttribute("totalItems", matriculasPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);

        return "admin/matriculas/lista_matriculas";
    }

    @GetMapping("/{id}")
    public String verDetalleMatricula(
            @PathVariable Integer id,
            Model model) {
        Optional<MatriculaDetalleDTO> matriculaDetalle = matriculaService.obtenerDetalleMatriculaPorId(id);
        if (matriculaDetalle.isPresent()) {
            model.addAttribute("matricula", matriculaDetalle.get());
            return "admin/matriculas/detalle_matricula";
        } else {
            // Handle not found case, e.g., redirect to an error page or list
            return "redirect:/matriculas?error=MatriculaNotFound";
        }
    }

    @PostMapping("/{id}/cambiar-estado")
    public String cambiarEstadoMatricula(
            @PathVariable Integer id,
            @RequestParam String nuevoEstado,
            RedirectAttributes redirectAttributes) {
        try {
            matriculaService.cambiarEstadoMatricula(id, nuevoEstado);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Estado de matrícula cambiado exitosamente a: " + nuevoEstado);
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al cambiar el estado de la matrícula: " + e.getMessage());
        }
        return "redirect:/admin/matricula";
    }
}
