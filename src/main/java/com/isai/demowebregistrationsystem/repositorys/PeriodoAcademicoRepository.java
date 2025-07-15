package com.isai.demowebregistrationsystem.repositorys;

import com.isai.demowebregistrationsystem.model.entities.PeriodoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodoAcademicoRepository
        extends JpaRepository<PeriodoAcademico, Integer> {

    Optional<PeriodoAcademico> findByNombrePeriodoAndAnoAcademico(String nombrePeriodo, Integer anoAcademico);

    List<PeriodoAcademico> findByActivoTrue();

    List<PeriodoAcademico> findByActivoTrueOrderByAnoAcademicoDescFechaInicioDesc();

    Optional<PeriodoAcademico> findByActivoTrueAndEstado(String estado);

    Optional<PeriodoAcademico> findByEstado(String estado);

}
