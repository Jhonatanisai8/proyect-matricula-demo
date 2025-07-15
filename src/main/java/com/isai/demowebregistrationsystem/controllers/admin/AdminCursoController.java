package com.isai.demowebregistrationsystem.controllers.admin;

import com.isai.demowebregistrationsystem.model.dtos.CursoDTO;
import com.isai.demowebregistrationsystem.model.dtos.GradoDTO;
import com.isai.demowebregistrationsystem.services.CursoService;
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

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/admin/cursos")
public class AdminCursoController {

    private final CursoService cursoService;

    private final GradoService gradoService;


    @GetMapping
    public String listarCursos(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CursoDTO> cursosPage = cursoService.obtenerCursos(pageable);
        model.addAttribute("cursos", cursosPage);
        return "admin/cursos/lista_cursos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrearCurso(Model model) {
        model.addAttribute("cursoDTO", new CursoDTO());
        model.addAttribute("isEdit", false);
        List<GradoDTO> gradosActivos = gradoService.obtenerTodosLosGradosActivos();
        model.addAttribute("gradosActivos", gradosActivos);
        return "admin/cursos/crear_curso";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarCurso(@PathVariable
                                               Integer id,
                                               Model model,
                                               RedirectAttributes redirectAttributes) {
        Optional<CursoDTO> cursoOptional = cursoService.obtenerCursoPorId(id);
        if (cursoOptional.isPresent()) {
            model.addAttribute("cursoDTO", cursoOptional.get());
            model.addAttribute("isEdit", true);
            List<GradoDTO> gradosActivos = gradoService.obtenerTodosLosGradosActivos();
            model.addAttribute("gradosActivos", gradosActivos);
            return "admin/cursos/crear_curso";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Curso no encontrado.");
            return "redirect:/admin/cursos";
        }
    }


    @PostMapping("/guardar")
    public String guardarCurso(@Valid @ModelAttribute("cursoDTO") CursoDTO cursoDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", cursoDTO.getIdCurso() != null && cursoDTO.getIdCurso() != 0);
            List<GradoDTO> gradosActivos = gradoService.obtenerTodosLosGradosActivos();
            model.addAttribute("gradosActivos", gradosActivos);
            return "admin/cursos/crear_curso";
        }

        try {
            cursoService.guardarCurso(cursoDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    cursoDTO.getIdCurso() == null || cursoDTO.getIdCurso() == 0 ? "Curso registrado exitosamente." : "Curso actualizado exitosamente.");
            return "redirect:/admin/cursos";
        } catch (IllegalArgumentException e) {
            // Manejo de errores específicos del servicio
            if (e.getMessage().contains("código")) {
                result.rejectValue("codigoCurso", "error.cursoDTO", e.getMessage());
            } else if (e.getMessage().contains("nombre") && e.getMessage().contains("grado")) {
                result.rejectValue("nombreCurso", "error.cursoDTO", e.getMessage());
            } else if (e.getMessage().contains("Grado")) {
                result.rejectValue("idGrado", "error.cursoDTO", e.getMessage());
            } else {
                result.rejectValue("idCurso", "error.cursoDTO", e.getMessage());
            }

            model.addAttribute("isEdit", cursoDTO.getIdCurso() != null && cursoDTO.getIdCurso() != 0);
            List<GradoDTO> gradosActivos = gradoService.obtenerTodosLosGradosActivos();
            model.addAttribute("gradosActivos", gradosActivos);
            return "admin/cursos/crear_curso";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar el curso: " + e.getMessage());
            return "redirect:/admin/cursos";
        }
    }

    @GetMapping("/detalle/{id}")
    public String verDetalleCurso(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<CursoDTO> cursoOptional = cursoService.obtenerCursoPorId(id);
        if (cursoOptional.isPresent()) {
            model.addAttribute("curso", cursoOptional.get());
            return "admin/cursos/detalle_curso";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Curso no encontrado.");
            return "redirect:/admin/cursos";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCurso(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            cursoService.eliminarCurso(id);
            redirectAttributes.addFlashAttribute("successMessage", "Curso eliminado exitosamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el curso: " + e.getMessage());
        }
        return "redirect:/admin/cursos";
    }

    @GetMapping("/toggleActivo/{id}")
    public String alternarEstadoCurso(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            cursoService.alternarEstadoCurso(id);
            redirectAttributes.addFlashAttribute("successMessage", "Estado del curso actualizado.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cambiar el estado del curso: " + e.getMessage());
        }
        return "redirect:/admin/cursos";
    }

}
