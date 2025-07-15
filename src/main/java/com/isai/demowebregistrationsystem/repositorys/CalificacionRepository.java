package com.isai.demowebregistrationsystem.repositorys;

import com.isai.demowebregistrationsystem.model.entities.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalificacionRepository
        extends JpaRepository<Calificacion, Integer> {

    Optional<Calificacion> findByEstudiante_IdEstudianteAndCurso_IdCursoAndPeriodoAcademico_IdPeriodo(
            Integer idEstudiante, Integer idCurso, Integer idPeriodoAcademico);

    List<Calificacion> findByCurso_IdCursoAndPeriodoAcademico_IdPeriodo(
            Integer idCurso, Integer idPeriodoAcademico);

    List<Calificacion> findByEstudiante_IdEstudianteAndPeriodoAcademico_IdPeriodo(Integer idEstudiante, Integer idPeriodoAcademico);
}
