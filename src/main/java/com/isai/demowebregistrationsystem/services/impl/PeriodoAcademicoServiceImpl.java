package com.isai.demowebregistrationsystem.services.impl;

import com.isai.demowebregistrationsystem.model.dtos.PeriodoAcademicoDTO;
import com.isai.demowebregistrationsystem.model.entities.PeriodoAcademico;
import com.isai.demowebregistrationsystem.repositorys.PeriodoAcademicoRepository;
import com.isai.demowebregistrationsystem.services.PeriodoAcademicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PeriodoAcademicoServiceImpl
        implements PeriodoAcademicoService {

    private final PeriodoAcademicoRepository periodoAcademicoRepository;

    private PeriodoAcademico convertToEntity(PeriodoAcademicoDTO periodoDTO) {
        PeriodoAcademico periodo = new PeriodoAcademico();
        BeanUtils.copyProperties(periodoDTO, periodo);
        return periodo;
    }

    private PeriodoAcademicoDTO convertToDto(PeriodoAcademico periodo) {
        PeriodoAcademicoDTO periodoDTO = new PeriodoAcademicoDTO();
        BeanUtils.copyProperties(periodo, periodoDTO);
        return periodoDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PeriodoAcademicoDTO> obtenerPeriodos(Pageable pageable) {
        return periodoAcademicoRepository.findAll(pageable).map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PeriodoAcademicoDTO> obtenerPeriodoPorId(Integer id) {
        return periodoAcademicoRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public PeriodoAcademicoDTO guardarPeriodo(PeriodoAcademicoDTO periodoDTO) {
        // Validación de fechas: fechaFin debe ser posterior a fechaInicio
        if (periodoDTO.getFechaInicio() != null && periodoDTO.getFechaFin() != null) {
            if (periodoDTO.getFechaFin().isBefore(periodoDTO.getFechaInicio())) {
                throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
            }
        }

        // Validación de período único (nombre + año académico)
        Optional<PeriodoAcademico> existingPeriodo = periodoAcademicoRepository.findByNombrePeriodoAndAnoAcademico(
                periodoDTO.getNombrePeriodo(), periodoDTO.getAnoAcademico());

        if (existingPeriodo.isPresent() &&
                (periodoDTO.getIdPeriodo() == null || !existingPeriodo.get().getIdPeriodo().equals(periodoDTO.getIdPeriodo()))) {
            throw new IllegalArgumentException("Ya existe un período académico con el nombre '" + periodoDTO.getNombrePeriodo() + "' y año '" + periodoDTO.getAnoAcademico() + "'.");
        }

        PeriodoAcademico periodo;
        if (periodoDTO.getIdPeriodo() == null || periodoDTO.getIdPeriodo() == 0) { // Creando nuevo período
            periodo = new PeriodoAcademico();
            // Los valores por defecto (activo, estado) se manejan en @PrePersist de la entidad
            periodo.setActivo(periodoDTO.getActivo() != null ? periodoDTO.getActivo() : true);
            periodo.setEstado(periodoDTO.getEstado() != null && !periodoDTO.getEstado().isEmpty() ? periodoDTO.getEstado() : "PENDIENTE");
        } else { // Editando período existente
            periodo = periodoAcademicoRepository.findById(periodoDTO.getIdPeriodo())
                    .orElseThrow(() -> new IllegalArgumentException("Período Académico con ID " + periodoDTO.getIdPeriodo() + " no encontrado."));
            // Actualizar el estado activo basado en el DTO (checkbox)
            periodo.setActivo(periodoDTO.getActivo() != null ? periodoDTO.getActivo() : false);
            // Actualizar estado si se provee
            periodo.setEstado(periodoDTO.getEstado() != null && !periodoDTO.getEstado().isEmpty() ? periodoDTO.getEstado() : periodo.getEstado());
        }

        BeanUtils.copyProperties(periodoDTO, periodo, "idPeriodo", "activo", "estado");

        return convertToDto(periodoAcademicoRepository.save(periodo));
    }

    @Override
    public void eliminarPeriodo(Integer id) {
        if (!periodoAcademicoRepository.existsById(id)) {
            throw new IllegalArgumentException("El período académico con ID " + id + " no existe.");
        }
        periodoAcademicoRepository.deleteById(id);
    }

    @Override
    public void alternarEstadoPeriodo(Integer id) {
        PeriodoAcademico periodo = periodoAcademicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Período Académico no encontrado con ID: " + id));
        periodo.setActivo(!periodo.getActivo());
        periodoAcademicoRepository.save(periodo);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PeriodoAcademicoDTO> obtenerTodosLosPeriodosActivos() {
        return periodoAcademicoRepository.findByActivoTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
