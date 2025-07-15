package com.isai.demowebregistrationsystem.controllers.docente;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.dtos.docente.EstudianteAsistenciaDTO;
import com.isai.demowebregistrationsystem.model.dtos.docente.RegistrarAsistenciaViewDTO;
import com.isai.demowebregistrationsystem.model.dtos.docente.RegistroAsistenciaRequestDTO;
import com.isai.demowebregistrationsystem.services.DocenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/docente")
public class DocenteAsistenciaController {

    private final DocenteService docenteService;


    @GetMapping("/registrar_asistencia")
    public String showRegistrarAsistenciaPage(Model model,
                                              Principal principal) {
        String username = principal.getName();
        RegistrarAsistenciaViewDTO viewData = docenteService.getRegistrarAsistenciaViewData(username);
        model.addAttribute("viewData", viewData);
        model.addAttribute("currentDate", LocalDate.now());
        return "docente/registrar_asistencias";
    }

    @GetMapping("/api/asistencia/estudiantes")
    @ResponseBody
    public ResponseEntity<List<EstudianteAsistenciaDTO>> getEstudiantesConAsistencia(
            @RequestParam("idHorario") Integer idHorario,
            @RequestParam("fechaAsistencia") LocalDate fechaAsistencia,
            Principal principal) {
        try {
            String username = principal.getName();
            List<EstudianteAsistenciaDTO> estudiantes = docenteService.getEstudiantesConAsistenciaActual(username, idHorario, fechaAsistencia);
            return ResponseEntity.ok(estudiantes);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/api/asistencia/registrar")
    @ResponseBody
    public ResponseEntity<String> registrarAsistencia(
            @Valid @RequestBody RegistroAsistenciaRequestDTO requestDTO,
            Principal principal) {
        try {
            String username = principal.getName();
            String mensaje = docenteService.registrarAsistencia(username, requestDTO);
            return ResponseEntity.ok(mensaje);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor al registrar asistencia: " + e.getMessage());
        }
    }

}
