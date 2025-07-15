package com.isai.demowebregistrationsystem.services;

import com.isai.demowebregistrationsystem.model.dtos.MatriculaDTO;
import com.isai.demowebregistrationsystem.model.dtos.MatriculaDetalleDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.MatriculaGestionDTO;
import com.isai.demowebregistrationsystem.model.dtos.opciones.PeriodoAcademicoOptionDTO;
import com.isai.demowebregistrationsystem.model.dtos.opciones.SeccionOptionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MatriculaService {

    MatriculaGestionDTO obtenerMatriculaParaGestion(Integer idEstudiante, Integer idPeriodoAcademico); // Para editar o crear una nueva

    void guardarMatricula(MatriculaGestionDTO matriculaDTO);

    List<PeriodoAcademicoOptionDTO> obtenerPeriodosAcademicosDisponibles();

    List<SeccionOptionDTO> obtenerSeccionesPorGradoYPeriodo(Integer idGrado, Integer idPeriodo);

    Map<String, String> getEstadosMatricula();

    Map<String, String> getModalidadesPago();

    Page<MatriculaDTO> listarTodasMatriculas(String searchTerm, Pageable pageable);

    Optional<MatriculaDetalleDTO> obtenerDetalleMatriculaPorId(Integer id);

    void cambiarEstadoMatricula(Integer idMatricula, String nuevoEstado);
}
