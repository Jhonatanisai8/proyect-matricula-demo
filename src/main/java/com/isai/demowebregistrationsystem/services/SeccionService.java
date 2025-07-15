package com.isai.demowebregistrationsystem.services;

import com.isai.demowebregistrationsystem.model.dtos.secciones.*;
import com.isai.demowebregistrationsystem.model.entities.*;
import com.isai.demowebregistrationsystem.model.entities.PeriodoAcademico;

import java.util.List;
import java.util.Optional;

public interface SeccionService {

    List<SeccionListadoDTO> listarTodasLasSecciones();

    Optional<SeccionDetalleDTO> obtenerSeccionPorId(Integer id);

    SeccionRegistroDTO guardarSeccion(SeccionRegistroDTO seccionDTO);

    void eliminarSeccion(Integer id);

    List<Docente> listarTodosDocentes();
    // asumimos que necesitamos el objeto Docente completo para el select

    List<Grado> listarTodosGrados();
    // asumimos que necesitamos el objeto Grado completo para el select

    List<PeriodoAcademico> listarTodosPeriodosAcademicos();
    // asumimos que necesitamos el objeto PeriodoAcademico completo

}
