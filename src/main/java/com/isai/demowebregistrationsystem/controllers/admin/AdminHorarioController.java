package com.isai.demowebregistrationsystem.controllers.admin;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.exceptions.ValidationException;
import com.isai.demowebregistrationsystem.model.dtos.horarios.HorarioDetalleDTO;
import com.isai.demowebregistrationsystem.model.dtos.horarios.HorarioRegistroDTO;
import com.isai.demowebregistrationsystem.services.HorarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/admin/horarios")
public class AdminHorarioController {

    private final HorarioService horarioService;

    private static final List<String> DIAS_SEMANA = Arrays.asList(
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"
    );

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("cursos", horarioService.listarTodosCursos());
        model.addAttribute("docentes", horarioService.listarTodosDocentes());
        model.addAttribute("grados", horarioService.listarTodosGrados());
        model.addAttribute("periodosAcademicos", horarioService.listarTodosPeriodosAcademicos());
        model.addAttribute("salones", horarioService.listarTodosSalones());
        model.addAttribute("secciones", horarioService.listarTodasSecciones());
        model.addAttribute("diasSemana", DIAS_SEMANA);
    }

    @GetMapping
    public String listarHorarios(Model model) {
        model.addAttribute("horarios", horarioService.listarTodosLosHorarios());
        return "admin/horarios/lista_horarios";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("horarioRegistroDTO", new HorarioRegistroDTO());
        cargarDatosFormulario(model);
        model.addAttribute("isEdit", false);
        return "admin/horarios/crear_horario";
    }

    @PostMapping("/crear")
    public String crearHorario(@Valid @ModelAttribute("horarioRegistroDTO") HorarioRegistroDTO horarioRegistroDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            cargarDatosFormulario(model);
            // Recargar datos para los dropdowns
            model.addAttribute("isEdit", false);
            return "admin/horarios/crear_horario";
        }

        try {
            horarioService.guardarHorario(horarioRegistroDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Horario creado exitosamente.");
            return "redirect:/admin/horarios";
        } catch (ValidationException e) {
            //errores de validacion
            result.reject(null, e.getMessage());
            cargarDatosFormulario(model);
            model.addAttribute("isEdit", false);
            return "admin/horarios/crear_horario";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al crear horario: " + e.getMessage());
            return "redirect:/admin/horarios/crear";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al crear horario: " + e.getMessage());
            return "redirect:/admin/horarios/crear";
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            HorarioDetalleDTO horarioDetalleDTO = horarioService.obtenerHorarioPorId(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado con ID: " + id));
            // Convertir HorarioDetalleDTO a HorarioRegistroDTO para el formulario
            HorarioRegistroDTO horarioRegistroDTO = new HorarioRegistroDTO(
                    horarioDetalleDTO.getIdHorario(),
                    horarioDetalleDTO.getActivo(),
                    horarioDetalleDTO.getDiaSemana(),
                    horarioDetalleDTO.getHoraInicio(),
                    horarioDetalleDTO.getHoraFin(),
                    horarioDetalleDTO.getObservaciones(),
                    horarioDetalleDTO.getTipoClase(),
                    horarioDetalleDTO.getIdCurso(),
                    horarioDetalleDTO.getIdDocente(),
                    horarioDetalleDTO.getIdGrado(),
                    horarioDetalleDTO.getIdPeriodoAcademico(),
                    horarioDetalleDTO.getIdSalon(),
                    horarioDetalleDTO.getIdSeccion()
            );

            model.addAttribute("horarioRegistroDTO", horarioRegistroDTO);
            cargarDatosFormulario(model);
            model.addAttribute("isEdit", true); // Para diferenciar en la vista
            return "admin/horarios/crear_horario";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/horarios";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarHorario(@PathVariable Integer id,
                                @Valid @ModelAttribute("horarioRegistroDTO") HorarioRegistroDTO horarioRegistroDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        // Asegurarse de que el ID del DTO coincida con el ID de la URL
        if (!id.equals(horarioRegistroDTO.getIdHorario())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: ID del horario no coincide.");
            return "redirect:/admin/horarios";
        }

        if (result.hasErrors()) {
            cargarDatosFormulario(model); // Recargar datos para los dropdowns
            model.addAttribute("isEdit", true);
            return "admin/horarios/crear_horario";
        }

        try {
            horarioService.guardarHorario(horarioRegistroDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Horario actualizado exitosamente.");
            return "redirect:/admin/horarios";
        } catch (ValidationException e) {
            result.reject(null, e.getMessage());
            cargarDatosFormulario(model);
            model.addAttribute("isEdit", true);
            return "admin/horarios/crear_horario";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar horario: " + e.getMessage());
            return "redirect:/admin/horarios/editar/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al actualizar horario: " + e.getMessage());
            return "redirect:/admin/horarios/editar/" + id;
        }
    }

    @GetMapping("/{id}")
    public String verDetalleHorario(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            HorarioDetalleDTO horario = horarioService.obtenerHorarioPorId(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado con ID: " + id));
            model.addAttribute("horario", horario);
            return "admin/horarios/detalle_horario";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/horarios";
        }
    }


    @PostMapping("/eliminar/{id}")
    public String eliminarHorario(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            horarioService.eliminarHorario(id);
            redirectAttributes.addFlashAttribute("successMessage", "Horario eliminado exitosamente.");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar horario: " + e.getMessage());
        }
        return "redirect:/admin/horarios";
    }
}
