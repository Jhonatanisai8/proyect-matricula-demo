package com.isai.demowebregistrationsystem.controllers.apoderado;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.exceptions.ValidationException;
import com.isai.demowebregistrationsystem.model.dtos.apoderado.EstudianteListaApoderadoDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.MatriculaGestionDTO;
import com.isai.demowebregistrationsystem.model.dtos.opciones.PeriodoAcademicoOptionDTO;
import com.isai.demowebregistrationsystem.model.dtos.opciones.SeccionOptionDTO;
import com.isai.demowebregistrationsystem.model.entities.Apoderado;
import com.isai.demowebregistrationsystem.model.entities.Persona;
import com.isai.demowebregistrationsystem.model.entities.Usuario;
import com.isai.demowebregistrationsystem.repositorys.ApoderadoRepository;
import com.isai.demowebregistrationsystem.repositorys.PersonaRepository;
import com.isai.demowebregistrationsystem.repositorys.UsuarioRepository;
import com.isai.demowebregistrationsystem.services.ApoderadoService;
import com.isai.demowebregistrationsystem.services.EstudianteService;
import com.isai.demowebregistrationsystem.services.MatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/apoderado")
public class ApoderadoListadoHijosController {

    private final ApoderadoService apoderadoService;
    private final MatriculaService matriculaService;
    private final EstudianteService estudianteService;
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final ApoderadoRepository apoderadoRepository;


    @GetMapping("/mis_hijos")
    public String showMisHijos(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            String usernameApoderado = principal.getName();
            List<EstudianteListaApoderadoDTO> hijos = apoderadoService.obtenerHijosDelApoderado(usernameApoderado);
            model.addAttribute("hijos", hijos);
            return "apoderado/mis_hijos";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Apoderado no encontrado. Por favor, intente de nuevo.");
            return "redirect:/logout";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cargar la lista de hijos: " + e.getMessage());
            return "redirect:/apoderado/dashboard";
        }
    }

    //para la matricula
    @GetMapping("/matricula/{idEstudiante}")
    public String gestionarMatricula(@PathVariable("idEstudiante") Integer idEstudiante,
                                     @RequestParam(name = "idPeriodo", required = false) Integer idPeriodo,
                                     Model model,
                                     Principal principal,
                                     RedirectAttributes redirectAttributes,
                                     @RequestParam(name = "success", required = false) String successMessage,
                                     @RequestParam(name = "error", required = false) String errorMessage) {
        //obtenemos el apoderado
        String nombreUsuario = principal.getName();
        Usuario usuario = usuarioRepository.findByUserName(nombreUsuario).get();
        Persona persona = usuario.getPersona();
        Integer idPersona = persona.getIdPersona();
        Apoderado apoderadoBD = apoderadoRepository.findByPersonaIdPersona(idPersona).get();

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
            model.addAttribute("estudianteId", idEstudiante);
            cargarDatosFormularioMatricula(model, matriculaDTO.getIdGrado(), matriculaDTO.getIdPeriodoAcademico(), apoderadoBD.getIdApoderado());

            if (successMessage != null) {
                model.addAttribute("successMessage", successMessage);
            }
            if (errorMessage != null) {
                model.addAttribute("errorMessage", errorMessage);
            }
            return "apoderado/gestionar_matricula";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/apoderado/mis_hijos";
        }
    }


    private void cargarDatosFormularioMatricula(Model model,
                                                Integer idGradoSeleccionado,
                                                Integer idPeriodoSeleccionado,
                                                Integer idApoderado) {
        model.addAttribute("periodosAcademicos", matriculaService.obtenerPeriodosAcademicosDisponibles());
        model.addAttribute("estadosMatricula", matriculaService.getEstadosMatricula());
        model.addAttribute("modalidadesPago", matriculaService.getModalidadesPago());
        model.addAttribute("grados", estudianteService.obtenerGradosDisponibles());
        model.addAttribute("idApoderado", idApoderado);

        Apoderado apoderadoBD = apoderadoRepository.findById(idApoderado).get();
        model.addAttribute("apoderadoBD", apoderadoBD);


        if (idGradoSeleccionado != null && idPeriodoSeleccionado != null) {
            model.addAttribute("secciones", matriculaService.obtenerSeccionesPorGradoYPeriodo(idGradoSeleccionado, idPeriodoSeleccionado));
        } else {
            model.addAttribute("secciones", List.of());
        }

        //model.addAttribute("apoderadosParaMatricula", estudianteService.obtenerApoderadosDisponibles());
    }

    @PostMapping("/guardar_matricula")
    public String guardarMatricula(
            @Valid @ModelAttribute("matricula") MatriculaGestionDTO matriculaDTO,
            BindingResult result,
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        //obtenemos el apoderado
        String nombreUsuario = principal.getName();
        Usuario usuario = usuarioRepository.findByUserName(nombreUsuario).get();
        Persona persona = usuario.getPersona();
        Integer idPersona = persona.getIdPersona();
        Apoderado apoderadoBD = apoderadoRepository.findByPersonaIdPersona(idPersona).get();

        if (result.hasErrors()) {
            model.addAttribute("estudianteId", matriculaDTO.getIdEstudiante());
            cargarDatosFormularioMatricula(model, matriculaDTO.getIdGrado(), matriculaDTO.getIdPeriodoAcademico(), apoderadoBD.getIdApoderado());
            model.addAttribute("errorMessage", "Por favor, corrige los errores en el formulario.");
            return "apoderado/gestionar_matricula";
        }
        try {
            matriculaService.guardarMatricula(matriculaDTO);
            String message = (matriculaDTO.getIdMatricula() == null) ? "Matrícula creada exitosamente." : "Matrícula actualizada exitosamente.";
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/apoderado/mis_hijos";
        } catch (ValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("estudianteId", matriculaDTO.getIdEstudiante());
            cargarDatosFormularioMatricula(model, matriculaDTO.getIdGrado(), matriculaDTO.getIdPeriodoAcademico(), apoderadoBD.getIdApoderado());
            return "apoderado/gestionar_matricula";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/apoderado/mis_hijos";
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
