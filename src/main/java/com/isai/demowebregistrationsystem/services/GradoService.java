package com.isai.demowebregistrationsystem.services;

import com.isai.demowebregistrationsystem.model.dtos.GradoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GradoService {

    Page<GradoDTO> obtenerGrados(Pageable pageable);

    Optional<GradoDTO> obtenerGradoPorId(Integer id);

    GradoDTO guardarGrado(GradoDTO gradoDTO);

    void eliminarGrado(Integer id);

    void alternarEstadoGrado(Integer id);

    List<GradoDTO> obtenerTodosLosGradosActivos();

}
