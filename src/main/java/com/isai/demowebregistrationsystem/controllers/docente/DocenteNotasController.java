package com.isai.demowebregistrationsystem.controllers.docente;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.dtos.docente.EstudianteNotaDTO;
import com.isai.demowebregistrationsystem.model.dtos.docente.RegistrarNotasViewDTO;
import com.isai.demowebregistrationsystem.model.dtos.docente.RegistroNotasRequestDTO;
import com.isai.demowebregistrationsystem.services.DocenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = "/docente")
@RequiredArgsConstructor
public class DocenteNotasController {

    private final DocenteService docenteService;

    @GetMapping("/registrar_notas")
    public String showRegistrarNotasPage(Model model,
                                         Principal principal) {
        String username = principal.getName();
        RegistrarNotasViewDTO viewData = docenteService.getRegistrarNotasViewData(username);
        model.addAttribute("viewData", viewData);
        return "docente/registrar_notas";
    }

    @GetMapping("/api/notas/estudiantes")
    @ResponseBody
    public ResponseEntity<List<EstudianteNotaDTO>> getEstudiantesConNotas(
            @RequestParam("idAsignacion") Integer idAsignacion,
            Principal principal) {
        String username = principal.getName();
        List<EstudianteNotaDTO> estudiantes = docenteService.getEstudiantesConNotasActuales(username, idAsignacion);
        return ResponseEntity.ok(estudiantes);
    }


    @PostMapping("/api/notas/registrar")
    @ResponseBody
    public ResponseEntity<String> registrarNotas(
            @Valid @RequestBody RegistroNotasRequestDTO requestDTO,
            Principal principal) {
        try {
            String username = principal.getName();
            String mensaje = docenteService.registrarNotas(username, requestDTO);
            return ResponseEntity.ok(mensaje);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error interno del servidor al registrar notas: " + e.getMessage());
        }
    }

}
