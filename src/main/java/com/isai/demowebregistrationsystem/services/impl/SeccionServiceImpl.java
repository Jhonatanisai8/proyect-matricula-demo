package com.isai.demowebregistrationsystem.services.impl;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.dtos.secciones.SeccionDetalleDTO;
import com.isai.demowebregistrationsystem.model.dtos.secciones.SeccionListadoDTO;
import com.isai.demowebregistrationsystem.model.dtos.secciones.SeccionRegistroDTO;
import com.isai.demowebregistrationsystem.model.entities.Docente;
import com.isai.demowebregistrationsystem.model.entities.Grado;
import com.isai.demowebregistrationsystem.model.entities.PeriodoAcademico;
import com.isai.demowebregistrationsystem.model.entities.Seccion;
import com.isai.demowebregistrationsystem.repositorys.*;
import com.isai.demowebregistrationsystem.services.SeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeccionServiceImpl
        implements SeccionService {

    private final SeccionRepository seccionRepository;

    private final DocenteRepository docenteRepository;

    private final GradoRepository gradoRepository;

    private final PeriodoAcademicoRepository periodoAcademicoRepository;


    @Override
    @Transactional(readOnly = true)
    public List<SeccionListadoDTO> listarTodasLasSecciones() {
        return seccionRepository.findAll().stream()
                .map(this::convertirASeccionListadoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SeccionDetalleDTO> obtenerSeccionPorId(Integer id) {
        return seccionRepository.findById(id)
                .map(this::convertirASeccionDetalleDTO);
    }

    @Override
    @Transactional
    public SeccionRegistroDTO guardarSeccion(SeccionRegistroDTO seccionDTO) {
        Seccion seccion;
        if (seccionDTO.getIdSeccion() != null) {
            seccion = seccionRepository.findById(seccionDTO.getIdSeccion())
                    .orElseThrow(() -> new ResourceNotFoundException("Sección no encontrada con ID: " + seccionDTO.getIdSeccion()));
        } else {
            seccion = new Seccion();
        }

        seccion.setNombreSeccion(seccionDTO.getNombreSeccion());

        // Manejar docenteTutor (puede ser nulo)
        if (seccionDTO.getIdDocenteTutor() != null) {
            Docente docenteTutor = docenteRepository.findById(seccionDTO.getIdDocenteTutor())
                    .orElseThrow(() -> new ResourceNotFoundException("Docente tutor no encontrado con ID: " + seccionDTO.getIdDocenteTutor()));
            seccion.setDocenteTutor(docenteTutor);
        } else {
            // asegurarmos que se setea a null si no hay tutor
            seccion.setDocenteTutor(null);
        }

        // Grado (obligatorio)
        Grado grado = gradoRepository.findById(seccionDTO.getIdGrado())
                .orElseThrow(() -> new ResourceNotFoundException("Grado no encontrado con ID: " + seccionDTO.getIdGrado()));
        seccion.setGrado(grado);

        // Periodo Academico (obligatorio)
        PeriodoAcademico periodoAcademico = periodoAcademicoRepository.findById(seccionDTO.getIdPeriodoAcademico())
                .orElseThrow(() -> new ResourceNotFoundException("Período académico no encontrado con ID: " + seccionDTO.getIdPeriodoAcademico()));
        seccion.setPeriodoAcademico(periodoAcademico);

        Seccion savedSeccion = seccionRepository.save(seccion);
        seccionDTO.setIdSeccion(savedSeccion.getIdSeccion()); // Asegura que el ID se devuelve para el DTO
        return seccionDTO;
    }

    @Override
    @Transactional
    public void eliminarSeccion(Integer id) {
        if (!seccionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sección no encontrada con ID: " + id);
        }
        seccionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Docente> listarTodosDocentes() {
        return docenteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Grado> listarTodosGrados() {
        return gradoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeriodoAcademico> listarTodosPeriodosAcademicos() {
        return periodoAcademicoRepository.findAll();
    }


    private SeccionListadoDTO convertirASeccionListadoDTO(Seccion seccion) {
        String nombreDocente = (seccion.getDocenteTutor() != null && seccion.getDocenteTutor().getPersona() != null) ?
                seccion.getDocenteTutor().getPersona().getNombres() + " " + seccion.getDocenteTutor().getPersona().getApellidos() : "N/A";
        return new SeccionListadoDTO(
                seccion.getIdSeccion(),
                seccion.getNombreSeccion(),
                seccion.getGrado().getNombreGrado(),
                seccion.getPeriodoAcademico().getNombrePeriodo() + " (" + seccion.getPeriodoAcademico().getAnoAcademico() + ")",
                nombreDocente
        );
    }

    private SeccionDetalleDTO convertirASeccionDetalleDTO(Seccion seccion) {
        String nombreDocente = (seccion.getDocenteTutor() != null && seccion.getDocenteTutor().getPersona() != null) ?
                seccion.getDocenteTutor().getPersona().getNombres() + " " + seccion.getDocenteTutor().getPersona().getApellidos() : "N/A";
        Integer idDocente = seccion.getDocenteTutor() != null ? seccion.getDocenteTutor().getIdDocente() : null;

        return new SeccionDetalleDTO(
                seccion.getIdSeccion(),
                seccion.getNombreSeccion(),
                idDocente,
                nombreDocente,
                seccion.getGrado().getIdGrado(),
                seccion.getGrado().getNombreGrado(),
                seccion.getPeriodoAcademico().getIdPeriodo(),
                seccion.getPeriodoAcademico().getNombrePeriodo() + " (" + seccion.getPeriodoAcademico().getAnoAcademico() + ")"
        );
    }
}
