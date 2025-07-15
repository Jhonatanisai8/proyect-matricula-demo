package com.isai.demowebregistrationsystem.controllers.admin;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.exceptions.ValidationException;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.EstudianteDetalleDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.EstudianteListadoDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.EstudianteRegistroDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.MatriculaGestionDTO;
import com.isai.demowebregistrationsystem.model.dtos.opciones.PeriodoAcademicoOptionDTO;
import com.isai.demowebregistrationsystem.model.dtos.opciones.SeccionOptionDTO;
import com.isai.demowebregistrationsystem.services.EstudianteService;
import com.isai.demowebregistrationsystem.services.MatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/admin/estudiantes")
public class AdminEstudianteController {

    private final EstudianteService estudianteService;

    private final MatriculaService matriculaService;

    @GetMapping("/lista")
    public String listarEstudiantes(
            Model model,
            @RequestParam(name = "success", required = false) String successMessage,
            @RequestParam(name = "error", required = false) String errorMessage,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "activo", required = false) String activoStr,
            @PageableDefault(size = 10, sort = "persona.apellidos", direction = Sort.Direction.ASC) Pageable pageable) {

        Boolean activo = null;
        if (activoStr != null && !activoStr.isEmpty()) {
            activo = Boolean.parseBoolean(activoStr);
        }

        String sortField = pageable.getSort().stream().findFirst().map(Sort.Order::getProperty).orElse(null);
        String sortDirection = pageable.getSort().stream().findFirst().map(order -> order.getDirection().name()).orElse(null);

        Page<EstudianteListadoDTO> estudiantesPage = estudianteService.listarEstudiantesPaginado(keyword, activo, pageable);

        model.addAttribute("estudiantesPage", estudiantesPage);


        model.addAttribute("currentPage", estudiantesPage.getNumber());
        model.addAttribute("totalPages", estudiantesPage.getTotalPages());
        model.addAttribute("totalItems", estudiantesPage.getTotalElements());
        model.addAttribute("pageSize", estudiantesPage.getSize());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection != null ? sortDirection.toLowerCase() : null);
        model.addAttribute("keyword", keyword);
        model.addAttribute("activo", activoStr != null ? activoStr : "");

        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        return "admin/estudiantes/lista_estudiantes";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalleEstudiante(@PathVariable("id") Integer id, Model model,
                                       @RequestParam(name = "success", required = false) String successMessage,
                                       @RequestParam(name = "error", required = false) String errorMessage,
                                       RedirectAttributes redirectAttributes) { // Added RedirectAttributes
        try {
            EstudianteDetalleDTO estudiante = estudianteService.obtenerEstudianteDetalle(id);
            model.addAttribute("estudiante", estudiante);
            if (successMessage != null) {
                model.addAttribute("successMessage", successMessage);
            }
            if (errorMessage != null) {
                model.addAttribute("errorMessage", errorMessage);
            }
            return "admin/estudiantes/detalle_estudiante";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/estudiantes/lista";
        }
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrearEstudiante(Model model) {
        model.addAttribute("estudiante", new EstudianteRegistroDTO());
        cargarDatosFormularioEstudiante(model); // Cargar listas para selects
        return "admin/estudiantes/crear_estudiante";
    }

    private void cargarDatosFormularioEstudiante(Model model) {
        model.addAttribute("apoderados", estudianteService.obtenerApoderadosDisponibles());
        model.addAttribute("grados", estudianteService.obtenerGradosDisponibles());
        model.addAttribute("estadosCiviles", estudianteService.getEstadosCiviles());
        model.addAttribute("generos", estudianteService.getGeneros());
        model.addAttribute("tiposDocumento", estudianteService.getTiposDocumento());
        model.addAttribute("tiposSangre", estudianteService.getTiposSangre());
        model.addAttribute("gradosAnteriores", estudianteService.getGradosAnteriores());
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarEstudiante(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            EstudianteRegistroDTO estudianteDTO = estudianteService.obtenerEstudianteParaEdicion(id);
            model.addAttribute("estudiante", estudianteDTO);
            cargarDatosFormularioEstudiante(model);
            return "admin/estudiantes/crear_estudiante";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/estudiantes/lista";
        }
    }

    @PostMapping("/guardar")
    public String guardarEstudiante(@Valid @ModelAttribute("estudiante") EstudianteRegistroDTO estudianteDTO,
                                    BindingResult result,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            cargarDatosFormularioEstudiante(model);
            model.addAttribute("errorMessage", "Por favor, corrige los errores en el formulario.");
            return "admin/estudiantes/crear_estudiante";
        }
        try {
            estudianteService.guardarEstudiante(estudianteDTO);
            String message = (estudianteDTO.getIdEstudiante() == null) ? "Estudiante creado exitosamente." : "Estudiante actualizado exitosamente.";
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/admin/estudiantes/lista";
        } catch (ValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            cargarDatosFormularioEstudiante(model);
            return "admin/estudiantes/crear_estudiante";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/estudiantes/lista";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarEstudiante(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            estudianteService.eliminarEstudiante(id);
            redirectAttributes.addFlashAttribute("successMessage", "Estudiante inactivado exitosamente.");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/estudiantes/lista";
    }

    @GetMapping("/matricula/{idEstudiante}")
    public String gestionarMatricula(@PathVariable("idEstudiante") Integer idEstudiante,
                                     @RequestParam(name = "idPeriodo", required = false) Integer idPeriodo,
                                     Model model,
                                     RedirectAttributes redirectAttributes,
                                     @RequestParam(name = "success", required = false) String successMessage,
                                     @RequestParam(name = "error", required = false) String errorMessage) {
        try {
            if (idPeriodo == null) {
                Optional<PeriodoAcademicoOptionDTO> currentPeriod = matriculaService.obtenerPeriodosAcademicosDisponibles().stream()
                        .filter(p -> p.getNombrePeriodo().contains(String.valueOf(LocalDate.now().getYear())))
                        .findFirst();
                if (currentPeriod.isPresent()) {
                    idPeriodo = currentPeriod.get().getId();
                }
            }

            MatriculaGestionDTO matriculaDTO = matriculaService.obtenerMatriculaParaGestion(idEstudiante, idPeriodo);
            model.addAttribute("matricula", matriculaDTO);
            model.addAttribute("estudianteId", idEstudiante); // Para saber de qué estudiante es la matrícula
            // Pasar el id del grado y periodo a cargarDatosFormularioMatricula para que pueda cargar las secciones
            cargarDatosFormularioMatricula(model, matriculaDTO.getIdGrado(), matriculaDTO.getIdPeriodoAcademico());

            if (successMessage != null) {
                model.addAttribute("successMessage", successMessage);
            }
            if (errorMessage != null) {
                model.addAttribute("errorMessage", errorMessage);
            }
            return "admin/estudiantes/gestionar_matricula";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/estudiantes/detalle/" + idEstudiante;
        }
    }


    private void cargarDatosFormularioMatricula(Model model, Integer idGradoSeleccionado, Integer idPeriodoSeleccionado) {
        model.addAttribute("periodosAcademicos", matriculaService.obtenerPeriodosAcademicosDisponibles());
        model.addAttribute("estadosMatricula", matriculaService.getEstadosMatricula());
        model.addAttribute("modalidadesPago", matriculaService.getModalidadesPago());
        model.addAttribute("grados", estudianteService.obtenerGradosDisponibles());

        // Cargar secciones dinámicamente si ya hay un grado y periodo seleccionados
        if (idGradoSeleccionado != null && idPeriodoSeleccionado != null) {
            model.addAttribute("secciones", matriculaService.obtenerSeccionesPorGradoYPeriodo(idGradoSeleccionado, idPeriodoSeleccionado));
        } else {
            model.addAttribute("secciones", List.of()); // Si no hay selecciones, enviar lista vacía
        }

        model.addAttribute("apoderadosParaMatricula", estudianteService.obtenerApoderadosDisponibles());
    }

    @PostMapping("/guardar_matricula")
    public String guardarMatricula(@Valid @ModelAttribute("matricula") MatriculaGestionDTO matriculaDTO,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("estudianteId", matriculaDTO.getIdEstudiante()); // Asegurar que el ID del estudiante esté en el modelo
            cargarDatosFormularioMatricula(model, matriculaDTO.getIdGrado(), matriculaDTO.getIdPeriodoAcademico());
            model.addAttribute("errorMessage", "Por favor, corrige los errores en el formulario.");
            return "admin/estudiantes/gestionar_matricula";
        }
        try {
            matriculaService.guardarMatricula(matriculaDTO);
            String message = (matriculaDTO.getIdMatricula() == null) ? "Matrícula creada exitosamente." : "Matrícula actualizada exitosamente.";
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/admin/estudiantes/detalle/" + matriculaDTO.getIdEstudiante();
        } catch (ValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("estudianteId", matriculaDTO.getIdEstudiante());
            cargarDatosFormularioMatricula(model, matriculaDTO.getIdGrado(), matriculaDTO.getIdPeriodoAcademico());
            return "admin/estudiantes/gestionar_matricula";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/estudiantes/detalle/" + matriculaDTO.getIdEstudiante();
        }
    }

    @GetMapping("/api/secciones")
    @ResponseBody
    public List<SeccionOptionDTO> getSeccionesByGradoAndPeriodo(
            @RequestParam(name = "idGrado", required = false) Integer idGrado,
            @RequestParam(name = "idPeriodo", required = false) Integer idPeriodo) {
        if (idGrado == null || idPeriodo == null) {
            return List.of();
        }
        return matriculaService.obtenerSeccionesPorGradoYPeriodo(idGrado, idPeriodo);
    }

}
