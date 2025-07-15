package com.isai.demowebregistrationsystem.services.impl;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.exceptions.ValidationException;
import com.isai.demowebregistrationsystem.model.dtos.MatriculaDTO;
import com.isai.demowebregistrationsystem.model.dtos.MatriculaDetalleDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.MatriculaGestionDTO;
import com.isai.demowebregistrationsystem.model.dtos.opciones.PeriodoAcademicoOptionDTO;
import com.isai.demowebregistrationsystem.model.dtos.opciones.SeccionOptionDTO;
import com.isai.demowebregistrationsystem.model.entities.*;
import com.isai.demowebregistrationsystem.repositorys.*;
import com.isai.demowebregistrationsystem.services.MatriculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatriculaServiceImpl
        implements MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final PeriodoAcademicoRepository periodoAcademicoRepository;
    private final GradoRepository gradoRepository;
    private final SeccionRepository seccionRepository;
    private final ApoderadoRepository apoderadoRepository;

    @Override
    @Transactional(readOnly = true)
    public MatriculaGestionDTO obtenerMatriculaParaGestion(Integer idEstudiante, Integer idPeriodoAcademico) {
        Optional<Matricula> matriculaOptional = matriculaRepository.findByEstudiante_IdEstudianteAndPeriodoAcademico_IdPeriodoAndEstadoMatricula(idEstudiante, idPeriodoAcademico, "ACTIVA"); // O el estado que consideres "actual"

        if (matriculaOptional.isPresent()) {
            return convertirAMatriculaGestionDTO(matriculaOptional.get());
        } else {
            Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                    .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + idEstudiante));

            MatriculaGestionDTO newMatricula = new MatriculaGestionDTO();
            newMatricula.setIdEstudiante(idEstudiante);
            newMatricula.setFechaMatricula(LocalDate.now()); // Fecha por defecto
            newMatricula.setEstadoMatricula("PENDIENTE"); // Estado inicial por defecto
            if (estudiante.getApoderadoPrincipal() != null) {
                newMatricula.setIdApoderadoRealiza(estudiante.getApoderadoPrincipal().getIdApoderado());
            }

            periodoAcademicoRepository.findByActivoTrueAndEstado("ACTUAL")
                    .ifPresent(periodo -> newMatricula.setIdPeriodoAcademico(periodo.getIdPeriodo()));

            newMatricula.setNumeroMatricula(generarNumeroMatriculaTemporal(idEstudiante)); // Implementar este método

            return newMatricula;
        }
    }

    @Override
    @Transactional
    public void guardarMatricula(MatriculaGestionDTO dto) {
        Estudiante estudiante = estudianteRepository.findById(dto.getIdEstudiante())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + dto.getIdEstudiante()));
        Grado grado = gradoRepository.findById(dto.getIdGrado())
                .orElseThrow(() -> new ResourceNotFoundException("Grado no encontrado con ID: " + dto.getIdGrado()));
        Seccion seccion = seccionRepository.findById(dto.getIdSeccion())
                .orElseThrow(() -> new ResourceNotFoundException("Sección no encontrada con ID: " + dto.getIdSeccion()));
        PeriodoAcademico periodo = periodoAcademicoRepository.findById(dto.getIdPeriodoAcademico())
                .orElseThrow(() -> new ResourceNotFoundException("Período Académico no encontrado con ID: " + dto.getIdPeriodoAcademico()));
        Apoderado apoderadoRealiza = apoderadoRepository.findById(dto.getIdApoderadoRealiza())
                .orElseThrow(() -> new ResourceNotFoundException("Apoderado que realiza la matrícula no encontrado con ID: " + dto.getIdApoderadoRealiza()));

        Matricula matricula;
        if (dto.getIdMatricula() != null) { // Es una edición
            matricula = matriculaRepository.findById(dto.getIdMatricula())
                    .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada con ID: " + dto.getIdMatricula()));
        } else {
            matricula = new Matricula();

            if (matriculaRepository.findByEstudiante_IdEstudianteAndPeriodoAcademico_IdPeriodoAndEstadoMatricula(
                    dto.getIdEstudiante(), dto.getIdPeriodoAcademico(), "ACTIVA").isPresent()) {
                throw new ValidationException("Ya existe una matrícula activa para este estudiante en el período seleccionado.");
            }
        }

        matricula.setEstudiante(estudiante);
        matricula.setNumeroMatricula(dto.getNumeroMatricula());
        matricula.setFechaMatricula(dto.getFechaMatricula());
        matricula.setEstadoMatricula(dto.getEstadoMatricula());
        matricula.setMontoMatricula(dto.getMontoMatricula());
        matricula.setMontoPension(dto.getMontoPension());
        matricula.setModalidadPago(dto.getModalidadPago());
        matricula.setDocumentosCompletos(dto.getDocumentosCompletos());
        matricula.setDocumentosPendientes(dto.getDocumentosPendientes());
        matricula.setObservaciones(dto.getObservaciones());
        matricula.setGrado(grado);
        matricula.setSeccion(seccion);
        matricula.setPeriodoAcademico(periodo);
        matricula.setApoderadoRealiza(apoderadoRealiza);

        matriculaRepository.save(matricula);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeriodoAcademicoOptionDTO> obtenerPeriodosAcademicosDisponibles() {
        return periodoAcademicoRepository.findByActivoTrueOrderByAnoAcademicoDescFechaInicioDesc().stream()
                .map(periodo -> new PeriodoAcademicoOptionDTO(periodo.getIdPeriodo(), periodo.getNombrePeriodo() + " (" + periodo.getAnoAcademico() + ")"))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeccionOptionDTO> obtenerSeccionesPorGradoYPeriodo(Integer idGrado, Integer idPeriodo) {
        return seccionRepository.findByGradoIdGradoAndPeriodoAcademicoIdPeriodo(idGrado, idPeriodo).stream()
                .map(seccion -> new SeccionOptionDTO(seccion.getIdSeccion(), seccion.getNombreSeccion()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getEstadosMatricula() {
        Map<String, String> estados = new LinkedHashMap<>();
        estados.put("ACTIVA", "Activa");
        estados.put("PENDIENTE", "Pendiente");
        estados.put("COMPLETADA", "Completada");
        estados.put("RETIRADA", "Retirada");
        estados.put("INACTIVA", "Inactiva");
        return estados;
    }

    @Override
    public Map<String, String> getModalidadesPago() {
        Map<String, String> modalidades = new LinkedHashMap<>();
        modalidades.put("MENSUAL", "Mensual");
        modalidades.put("TRIMESTRAL", "Trimestral");
        modalidades.put("SEMESTRAL", "Semestral");
        modalidades.put("ANUAL", "Anual");
        return modalidades;
    }

    private MatriculaGestionDTO convertirAMatriculaGestionDTO(Matricula matricula) {
        MatriculaGestionDTO dto = new MatriculaGestionDTO();
        dto.setIdMatricula(matricula.getIdMatricula());
        dto.setIdEstudiante(matricula.getEstudiante().getIdEstudiante());
        dto.setNumeroMatricula(matricula.getNumeroMatricula());
        dto.setFechaMatricula(matricula.getFechaMatricula());
        dto.setEstadoMatricula(matricula.getEstadoMatricula());
        dto.setMontoMatricula(matricula.getMontoMatricula());
        dto.setMontoPension(matricula.getMontoPension());
        dto.setModalidadPago(matricula.getModalidadPago());
        dto.setDocumentosCompletos(matricula.getDocumentosCompletos());
        dto.setDocumentosPendientes(matricula.getDocumentosPendientes());
        dto.setObservaciones(matricula.getObservaciones());
        dto.setIdGrado(matricula.getGrado().getIdGrado());
        dto.setIdSeccion(matricula.getSeccion().getIdSeccion());
        dto.setIdPeriodoAcademico(matricula.getPeriodoAcademico().getIdPeriodo());
        dto.setIdApoderadoRealiza(matricula.getApoderadoRealiza().getIdApoderado());
        return dto;
    }

    private String generarNumeroMatriculaTemporal(Integer idEstudiante) {
        return "MAT-" + idEstudiante + "-" + LocalDate.now().getYear() + "-" + System.currentTimeMillis() % 1000;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MatriculaDTO> listarTodasMatriculas(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return matriculaRepository.findAll(pageable)
                    .map(this::convertToMatriculaDTO);
        } else {
            String lowerCaseSearchTerm = searchTerm.toLowerCase();
            return matriculaRepository.findByNumeroMatriculaContainingIgnoreCaseOrEstudiante_Persona_NombresContainingIgnoreCaseOrEstudiante_Persona_ApellidosContainingIgnoreCase(
                            lowerCaseSearchTerm, lowerCaseSearchTerm, lowerCaseSearchTerm, pageable)
                    .map(this::convertToMatriculaDTO);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<MatriculaDetalleDTO> obtenerDetalleMatriculaPorId(Integer id) {
        return matriculaRepository.findById(id)
                .map(this::convertToMatriculaDetalleDTO);
    }

    @Override
    @Transactional
    public void cambiarEstadoMatricula(Integer idMatricula, String nuevoEstado) {
        Matricula matricula = matriculaRepository.findById(idMatricula)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada con ID: " + idMatricula));
        
        // Validar que el nuevo estado sea válido
        List<String> estadosValidos = List.of("ACTIVA", "PENDIENTE", "COMPLETADA", "RETIRADA", "INACTIVA");
        if (!estadosValidos.contains(nuevoEstado)) {
            throw new ValidationException("Estado de matrícula no válido: " + nuevoEstado);
        }
        
        matricula.setEstadoMatricula(nuevoEstado);
        matriculaRepository.save(matricula);
    }


    private MatriculaDTO convertToMatriculaDTO(Matricula matricula) {
        String nombreEstudiante = matricula.getEstudiante() != null && matricula.getEstudiante().getPersona() != null ?
                matricula.getEstudiante().getPersona().getNombres() : "N/A";
        String apellidoEstudiante = matricula.getEstudiante() != null && matricula.getEstudiante().getPersona() != null ?
                matricula.getEstudiante().getPersona().getApellidos() : "N/A";
        String nombreGrado = matricula.getGrado() != null ? matricula.getGrado().getNombreGrado() : "N/A";
        String nombrePeriodoAcademico = matricula.getPeriodoAcademico() != null ? matricula.getPeriodoAcademico().getNombrePeriodo() : "N/A";
        String nombreApoderadoRealiza = matricula.getApoderadoRealiza() != null && matricula.getApoderadoRealiza().getPersona() != null ?
                matricula.getApoderadoRealiza().getPersona().getNombres() + " " + matricula.getApoderadoRealiza().getPersona().getApellidos() : "N/A";

        return new MatriculaDTO(
                matricula.getIdMatricula(),
                matricula.getNumeroMatricula(),
                matricula.getFechaMatricula(),
                matricula.getEstadoMatricula(),
                nombreEstudiante,
                apellidoEstudiante,
                nombreGrado,
                nombrePeriodoAcademico,
                nombreApoderadoRealiza
        );
    }


    private MatriculaDetalleDTO convertToMatriculaDetalleDTO(Matricula matricula) {
        String nombreCompletoEstudiante = matricula.getEstudiante() != null && matricula.getEstudiante().getPersona() != null ?
                matricula.getEstudiante().getPersona().getNombres() + " " + matricula.getEstudiante().getPersona().getApellidos() : "N/A";
        String codigoEstudiante = matricula.getEstudiante() != null ? matricula.getEstudiante().getCodigoEstudiante() : "N/A";
        String nombreGrado = matricula.getGrado() != null ? matricula.getGrado().getNombreGrado() : "N/A";
        String nombreSeccion = matricula.getSeccion() != null ? matricula.getSeccion().getNombreSeccion() : "N/A";
        String nombrePeriodoAcademico = matricula.getPeriodoAcademico() != null ? matricula.getPeriodoAcademico().getNombrePeriodo() : "N/A";
        String nombreApoderadoRealiza = matricula.getApoderadoRealiza() != null && matricula.getApoderadoRealiza().getPersona() != null ?
                matricula.getApoderadoRealiza().getPersona().getNombres() + " " + matricula.getApoderadoRealiza().getPersona().getApellidos() : "N/A";
        String dniApoderadoRealiza = matricula.getApoderadoRealiza() != null && matricula.getApoderadoRealiza().getPersona() != null ?
                matricula.getApoderadoRealiza().getPersona().getDni() : "N/A";

        return new MatriculaDetalleDTO(
                matricula.getIdMatricula(),
                matricula.getNumeroMatricula(),
                matricula.getFechaMatricula(),
                matricula.getEstadoMatricula(),
                matricula.getDocumentosPendientes(),
                matricula.getModalidadPago(),
                matricula.getMontoMatricula(),
                matricula.getMontoPension(),
                matricula.getObservaciones(),
                nombreCompletoEstudiante,
                codigoEstudiante,
                nombreGrado,
                nombreSeccion,
                nombrePeriodoAcademico,
                nombreApoderadoRealiza,
                dniApoderadoRealiza
        );
    }
}
