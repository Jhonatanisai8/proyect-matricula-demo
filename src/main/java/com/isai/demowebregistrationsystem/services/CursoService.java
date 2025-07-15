package com.isai.demowebregistrationsystem.services;

import com.isai.demowebregistrationsystem.model.dtos.CursoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CursoService {

    Page<CursoDTO> obtenerCursos(Pageable pageable);

    Optional<CursoDTO> obtenerCursoPorId(Integer id);

    CursoDTO guardarCurso(CursoDTO cursoDTO);

    void eliminarCurso(Integer id);

    void alternarEstadoCurso(Integer id);

}
