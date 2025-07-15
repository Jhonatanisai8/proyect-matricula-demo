package com.isai.demowebregistrationsystem.controllers.docente;

import com.isai.demowebregistrationsystem.model.dtos.docente.EstudianteListaDTO;
import com.isai.demowebregistrationsystem.model.dtos.docente.MisEstudiantesViewDTO;
import com.isai.demowebregistrationsystem.services.DocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping(path = "/docente")
@RequiredArgsConstructor
public class DocenteEstudiantesController {

    private final DocenteService docenteService;

    @GetMapping("/mis_estudiantes")
    public String mostrarMisEstudiantesPage(Model model,
                                            Principal principal) {
        String username = principal.getName();
        MisEstudiantesViewDTO viewData = docenteService.getMisEstudiantesViewData(username);
        model.addAttribute("viewData", viewData);
        return "docente/mis_estudiantes";
    }

    @GetMapping("/api/estudiantes-por-asignacion")
    @ResponseBody
    public ResponseEntity<Page<EstudianteListaDTO>> getEstudiantesPorAsignacion(
            @RequestParam("idAsignacion") Integer idAsignacion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal) {
        String username = principal.getName();
        Page<EstudianteListaDTO> estudiantesPaginados = docenteService.getEstudiantesPorAsignacion(username, idAsignacion, page, size);
        return ResponseEntity.ok(estudiantesPaginados);
    }

}
