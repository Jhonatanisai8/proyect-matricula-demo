package com.isai.demowebregistrationsystem.repositorys;


import com.isai.demowebregistrationsystem.model.entities.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioRepository
        extends JpaRepository<Horario, Integer> {

    List<Horario> findByDiaSemanaAndDocente_IdDocenteAndPeriodoAcademico_IdPeriodo(String diaSemana, Integer docenteId, Integer periodoAcademicoId);

    List<Horario> findByDiaSemanaAndSalon_IdSalonAndPeriodoAcademico_IdPeriodo(String diaSemana, Integer salonId, Integer periodoAcademicoId);

    List<Horario> findByDiaSemanaAndSeccion_IdSeccionAndGrado_IdGradoAndPeriodoAcademico_IdPeriodo(String diaSemana, Integer seccionId, Integer gradoId, Integer periodoAcademicoId);

    List<Horario> findByDiaSemanaAndDocente_IdDocenteAndPeriodoAcademico_IdPeriodoAndIdHorarioIsNot(String diaSemana, Integer docenteId, Integer periodoAcademicoId, Integer idHorario);

    List<Horario> findByDiaSemanaAndSalon_IdSalonAndPeriodoAcademico_IdPeriodoAndIdHorarioIsNot(String diaSemana, Integer salonId, Integer periodoAcademicoId, Integer idHorario);

    List<Horario> findByDiaSemanaAndSeccion_IdSeccionAndGrado_IdGradoAndPeriodoAcademico_IdPeriodoAndIdHorarioIsNot(String diaSemana, Integer seccionId, Integer gradoId, Integer periodoAcademicoId, Integer idHorario);

    List<Horario> findByCurso_IdCursoAndDocente_IdDocenteAndGrado_IdGradoAndPeriodoAcademico_IdPeriodo(
            Integer idCurso,
            Integer idDocente,
            Integer idGrado,
            Integer idPeriodoAcademico
    );

    List<Horario> findByDocente_IdDocenteAndActivoTrueAndPeriodoAcademico_ActivoTrue(Integer idDocente);

    Optional<Horario> findByIdHorarioAndDocente_IdDocente(Integer idHorario, Integer idDocente);

    /*PARA EL ROL DE ESTUDIANTE*/
    /*Buscamos los  horarios activos para un curso, grado, sección y periodo académico específico*/
    List<Horario> findByCurso_IdCursoAndGrado_IdGradoAndSeccion_IdSeccionAndPeriodoAcademico_IdPeriodoAndActivoTrue(
            Integer idCurso, Integer idGrado, Integer idSeccion, Integer idPeriodoAcademico);

    /*en caso un curso este en multiples horarios  */
//    List<Horario> findByCurso_IdCursoAndGrado_IdGradoAndSeccion_IdSeccionAndPeriodoAcademico_IdPeriodo(
//            Integer idCurso, Integer idGrado, Integer idSeccion, Integer idPeriodoAcademico);

    List<Horario> findByGrado_IdGradoAndSeccion_IdSeccionAndPeriodoAcademico_IdPeriodoAndActivoTrue(Integer idGradoMatriculado, Integer idSeccionMatriculada, Integer idPeriodoAcademicoMatriculado);


}