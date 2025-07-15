package com.isai.demowebregistrationsystem.services;

import com.isai.demowebregistrationsystem.model.dtos.horarios.*;
import com.isai.demowebregistrationsystem.model.entities.*;

import java.util.List;
import java.util.Optional;

public interface HorarioService {

    List<HorarioListadoDTO> listarTodosLosHorarios();

    Optional<HorarioDetalleDTO> obtenerHorarioPorId(Integer id);

    HorarioRegistroDTO guardarHorario(HorarioRegistroDTO horarioDTO);

    void eliminarHorario(Integer id);

    // cargar datos para los dropdowns del formulario
    List<Curso> listarTodosCursos();

    List<Docente> listarTodosDocentes();

    List<Grado> listarTodosGrados();

    List<PeriodoAcademico> listarTodosPeriodosAcademicos();

    List<Salon> listarTodosSalones();

    List<Seccion> listarTodasSecciones();

}
