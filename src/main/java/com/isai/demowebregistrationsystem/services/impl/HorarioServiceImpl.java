package com.isai.demowebregistrationsystem.services.impl;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.exceptions.ValidationException;
import com.isai.demowebregistrationsystem.model.dtos.horarios.HorarioDetalleDTO;
import com.isai.demowebregistrationsystem.model.dtos.horarios.HorarioListadoDTO;
import com.isai.demowebregistrationsystem.model.dtos.horarios.HorarioRegistroDTO;
import com.isai.demowebregistrationsystem.model.entities.*;
import com.isai.demowebregistrationsystem.repositorys.*;
import com.isai.demowebregistrationsystem.services.HorarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioServiceImpl
        implements HorarioService {

    private final HorarioRepository horarioRepository;
    private final CursoRepository cursoRepository;
    private final DocenteRepository docenteRepository;
    private final GradoRepository gradoRepository;
    private final PeriodoAcademicoRepository periodoAcademicoRepository;
    private final SalonRepository salonRepository;
    private final SeccionRepository seccionRepository;

    //horarios de turnos definidos
    private static final LocalTime MANANA_INICIO = LocalTime.of(7, 0);
    private static final LocalTime MANANA_FIN = LocalTime.of(13, 0);
    private static final LocalTime NOCHE_INICIO = LocalTime.of(18, 0);
    private static final LocalTime NOCHE_FIN = LocalTime.of(22, 0);

    private static final List<String> DIAS_SEMANA = Arrays.asList(
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"
    );

    private HorarioDetalleDTO convertirAHorarioDetalleDTO(Horario horario) {
        String nombreDocente = (horario.getDocente() != null && horario.getDocente().getPersona() != null) ?
                horario.getDocente().getPersona().getNombres() + " " + horario.getDocente().getPersona().getApellidos() : "N/A";
        String nombrePeriodo = (horario.getPeriodoAcademico() != null) ?
                horario.getPeriodoAcademico().getNombrePeriodo() + " (" + horario.getPeriodoAcademico().getAnoAcademico() + ")" : "N/A";

        return new HorarioDetalleDTO(
                horario.getIdHorario(),
                horario.getActivo(),
                horario.getDiaSemana(),
                horario.getHoraInicio(),
                horario.getHoraFin(),
                horario.getObservaciones(),
                horario.getTipoClase(),
                horario.getCurso().getIdCurso(),
                horario.getCurso().getNombreCurso(),
                horario.getDocente().getIdDocente(),
                nombreDocente,
                horario.getGrado().getIdGrado(),
                horario.getGrado().getNombreGrado(),
                horario.getPeriodoAcademico().getIdPeriodo(),
                nombrePeriodo,
                horario.getSalon().getIdSalon(),
                horario.getSalon().getNombreSalon(),
                horario.getSeccion().getIdSeccion(),
                horario.getSeccion().getNombreSeccion()
        );
    }

    private HorarioListadoDTO convertirAHorarioListadoDTO(Horario horario) {
        String nombreDocente = (horario.getDocente() != null && horario.getDocente().getPersona() != null) ?
                horario.getDocente().getPersona().getNombres() + " " + horario.getDocente().getPersona().getApellidos() : "N/A";
        String nombrePeriodo = (horario.getPeriodoAcademico() != null) ?
                horario.getPeriodoAcademico().getNombrePeriodo() + " (" + horario.getPeriodoAcademico().getAnoAcademico() + ")" : "N/A";

        return new HorarioListadoDTO(
                horario.getIdHorario(),
                horario.getDiaSemana(),
                horario.getHoraInicio(),
                horario.getHoraFin(),
                horario.getCurso().getNombreCurso(),
                nombreDocente,
                horario.getGrado().getNombreGrado(),
                nombrePeriodo,
                horario.getSalon().getNombreSalon(),
                horario.getSeccion().getNombreSeccion(),
                horario.getActivo()
        );
    }

    private void validarHorario(HorarioRegistroDTO horarioDTO) {
        if (horarioDTO.getHoraInicio().isAfter(horarioDTO.getHoraFin()) || horarioDTO.getHoraInicio().equals(horarioDTO.getHoraFin())) {
            throw new ValidationException("La hora de inicio debe ser anterior a la hora de fin.");
        }

        // Validación de turnos (Mañana y Noche)
        boolean isManana = (horarioDTO.getHoraInicio().isAfter(MANANA_INICIO) || horarioDTO.getHoraInicio().equals(MANANA_INICIO)) &&
                (horarioDTO.getHoraFin().isBefore(MANANA_FIN) || horarioDTO.getHoraFin().equals(MANANA_FIN));
        boolean isNoche = (horarioDTO.getHoraInicio().isAfter(NOCHE_INICIO) || horarioDTO.getHoraInicio().equals(NOCHE_INICIO)) &&
                (horarioDTO.getHoraFin().isBefore(NOCHE_FIN) || horarioDTO.getHoraFin().equals(NOCHE_FIN));

        if (!isManana && !isNoche) {
            throw new ValidationException("El horario debe estar dentro del turno de Mañana (07:00-13:00) o Noche (18:00-22:00).");
        }
        if (isManana && isNoche) {
            throw new ValidationException("El horario no puede abarcar ambos turnos (Mañana y Noche).");
        }

        // Configurar tipoClase basado en el turno
        horarioDTO.setTipoClase(isManana ? "Mañana" : "Noche");

        // Validar solapamiento de horarios para docente, salón y sección/grado
        Integer currentHorarioId = horarioDTO.getIdHorario(); // Será null para creación, ID para edición

        // Solapamiento para el DOCENTE
        List<Horario> horariosDocente;
        if (currentHorarioId != null) {
            horariosDocente = horarioRepository.findByDiaSemanaAndDocente_IdDocenteAndPeriodoAcademico_IdPeriodoAndIdHorarioIsNot(
                    horarioDTO.getDiaSemana(), horarioDTO.getIdDocente(), horarioDTO.getIdPeriodoAcademico(), currentHorarioId);
        } else {
            horariosDocente = horarioRepository.findByDiaSemanaAndDocente_IdDocenteAndPeriodoAcademico_IdPeriodo(
                    horarioDTO.getDiaSemana(), horarioDTO.getIdDocente(), horarioDTO.getIdPeriodoAcademico());
        }
        if (horariosDocente.stream().anyMatch(h ->
                (horarioDTO.getHoraInicio().isBefore(h.getHoraFin()) && horarioDTO.getHoraFin().isAfter(h.getHoraInicio()))
        )) {
            throw new ValidationException("El docente ya tiene un horario asignado que se solapa en este día y período.");
        }

        // Solapamiento para el SALÓN
        List<Horario> horariosSalon;
        if (currentHorarioId != null) {
            horariosSalon = horarioRepository.findByDiaSemanaAndSalon_IdSalonAndPeriodoAcademico_IdPeriodoAndIdHorarioIsNot(
                    horarioDTO.getDiaSemana(), horarioDTO.getIdSalon(), horarioDTO.getIdPeriodoAcademico(), currentHorarioId);
        } else {
            horariosSalon = horarioRepository.findByDiaSemanaAndSalon_IdSalonAndPeriodoAcademico_IdPeriodo(
                    horarioDTO.getDiaSemana(), horarioDTO.getIdSalon(), horarioDTO.getIdPeriodoAcademico());
        }
        if (horariosSalon.stream().anyMatch(h ->
                (horarioDTO.getHoraInicio().isBefore(h.getHoraFin()) && horarioDTO.getHoraFin().isAfter(h.getHoraInicio()))
        )) {
            throw new ValidationException("El salón ya está ocupado en este horario, día y período.");
        }

        // Solapamiento para la SECCIÓN/GRADO
        List<Horario> horariosSeccion;
        if (currentHorarioId != null) {
            horariosSeccion = horarioRepository.findByDiaSemanaAndSeccion_IdSeccionAndGrado_IdGradoAndPeriodoAcademico_IdPeriodoAndIdHorarioIsNot(
                    horarioDTO.getDiaSemana(), horarioDTO.getIdSeccion(), horarioDTO.getIdGrado(), horarioDTO.getIdPeriodoAcademico(), currentHorarioId);
        } else {
            horariosSeccion = horarioRepository.findByDiaSemanaAndSeccion_IdSeccionAndGrado_IdGradoAndPeriodoAcademico_IdPeriodo(
                    horarioDTO.getDiaSemana(), horarioDTO.getIdSeccion(), horarioDTO.getIdGrado(), horarioDTO.getIdPeriodoAcademico());
        }
        if (horariosSeccion.stream().anyMatch(h ->
                (horarioDTO.getHoraInicio().isBefore(h.getHoraFin()) && horarioDTO.getHoraFin().isAfter(h.getHoraInicio()))
        )) {
            throw new ValidationException("La sección para este grado ya tiene una clase en este horario, día y período.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<HorarioListadoDTO> listarTodosLosHorarios() {
        return horarioRepository.findAll()
                .stream()
                .map(this::convertirAHorarioListadoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HorarioDetalleDTO> obtenerHorarioPorId(Integer id) {
        return horarioRepository.findById(id)
                .map(this::convertirAHorarioDetalleDTO);
    }

    @Override
    @Transactional
    public HorarioRegistroDTO guardarHorario(HorarioRegistroDTO horarioDTO) {
        //primero validamos
        validarHorario(horarioDTO);

        Horario horario;
        if (horarioDTO.getIdHorario() != null) {
            horario = horarioRepository.findById(horarioDTO.getIdHorario())
                    .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado con ID: " + horarioDTO.getIdHorario()));
        } else {
            horario = new Horario();
        }

        horario.setActivo(horarioDTO.getActivo());
        horario.setDiaSemana(horarioDTO.getDiaSemana());
        horario.setHoraInicio(horarioDTO.getHoraInicio());
        horario.setHoraFin(horarioDTO.getHoraFin());
        horario.setObservaciones(horarioDTO.getObservaciones());
        horario.setTipoClase(horarioDTO.getTipoClase());

        // Cargamos entidades involucradas
        horario.setCurso(cursoRepository.findById(horarioDTO.getIdCurso())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con ID: " + horarioDTO.getIdCurso())));
        horario.setDocente(docenteRepository.findById(horarioDTO.getIdDocente())
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con ID: " + horarioDTO.getIdDocente())));
        horario.setGrado(gradoRepository.findById(horarioDTO.getIdGrado())
                .orElseThrow(() -> new ResourceNotFoundException("Grado no encontrado con ID: " + horarioDTO.getIdGrado())));
        horario.setPeriodoAcademico(periodoAcademicoRepository.findById(horarioDTO.getIdPeriodoAcademico())
                .orElseThrow(() -> new ResourceNotFoundException("Período Académico no encontrado con ID: " + horarioDTO.getIdPeriodoAcademico())));
        horario.setSalon(salonRepository.findById(horarioDTO.getIdSalon())
                .orElseThrow(() -> new ResourceNotFoundException("Salón no encontrado con ID: " + horarioDTO.getIdSalon())));
        horario.setSeccion(seccionRepository.findById(horarioDTO.getIdSeccion())
                .orElseThrow(() -> new ResourceNotFoundException("Sección no encontrada con ID: " + horarioDTO.getIdSeccion())));

        Horario savedHorario = horarioRepository.save(horario);
        horarioDTO.setIdHorario(savedHorario.getIdHorario());
        return horarioDTO;
    }

    @Override
    @Transactional
    public void eliminarHorario(Integer id) {
        if (!horarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Horario no encontrado con ID: " + id);
        }
        horarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listarTodosCursos() {
        return cursoRepository.findAll();
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

    @Override
    @Transactional(readOnly = true)
    public List<Salon> listarTodosSalones() {
        return salonRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seccion> listarTodasSecciones() {
        return seccionRepository.findAll();
    }
}
