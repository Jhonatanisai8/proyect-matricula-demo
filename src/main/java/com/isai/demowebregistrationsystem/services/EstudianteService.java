package com.isai.demowebregistrationsystem.services;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.EstudianteDetalleDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.EstudianteListadoDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.EstudianteRegistroDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante.CursosEstudianteViewDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante.EstudianteDashboardDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante.HorarioEstudianteViewDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante.NotasEstudianteViewDTO;
import com.isai.demowebregistrationsystem.model.dtos.opciones.ApoderadoOptionDTO;
import com.isai.demowebregistrationsystem.model.dtos.opciones.GradoOptionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface EstudianteService {

    Page<EstudianteListadoDTO> listarEstudiantesPaginado(String keyword, Boolean activo, Pageable pageable);

    EstudianteDetalleDTO obtenerEstudianteDetalle(Integer idEstudiante);

    EstudianteRegistroDTO obtenerEstudianteParaEdicion(Integer idEstudiante);

    void guardarEstudiante(EstudianteRegistroDTO estudianteDTO);

    // Crear o actualizar

    void eliminarEstudiante(Integer idEstudiante);


    // Métodos para cargar opciones en los formularios (selects)
    List<ApoderadoOptionDTO> obtenerApoderadosDisponibles();

    List<GradoOptionDTO> obtenerGradosDisponibles();

    Map<String, String> getEstadosCiviles();

    Map<String, String> getGeneros();

    Map<String, String> getTiposDocumento();

    Map<String, String> getTiposSangre();

    Map<String, String> getGradosAnteriores();

    /*METODOS PARA ROL DE ESTUDIANTE*/
    EstudianteDashboardDTO obtenerDatosDashboardEstudiante(String username) throws ResourceNotFoundException;

    CursosEstudianteViewDTO obtenerMisCursos(String username) throws ResourceNotFoundException;

    NotasEstudianteViewDTO obtenerMisNotas(String username) throws ResourceNotFoundException;

    HorarioEstudianteViewDTO obtenerMiHorario(String username) throws ResourceNotFoundException;


}

