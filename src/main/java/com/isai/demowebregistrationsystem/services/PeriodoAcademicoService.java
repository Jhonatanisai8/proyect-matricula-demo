package com.isai.demowebregistrationsystem.services;

import com.isai.demowebregistrationsystem.model.dtos.PeriodoAcademicoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PeriodoAcademicoService {

    Page<PeriodoAcademicoDTO> obtenerPeriodos(Pageable pageable);

    Optional<PeriodoAcademicoDTO> obtenerPeriodoPorId(Integer id);

    PeriodoAcademicoDTO guardarPeriodo(PeriodoAcademicoDTO periodoDTO);

    void eliminarPeriodo(Integer id);

    void alternarEstadoPeriodo(Integer id);

    List<PeriodoAcademicoDTO> obtenerTodosLosPeriodosActivos();

}
