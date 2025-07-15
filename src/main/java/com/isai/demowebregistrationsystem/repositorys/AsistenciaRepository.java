package com.isai.demowebregistrationsystem.repositorys;

import com.isai.demowebregistrationsystem.model.entities.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository
        extends JpaRepository<Asistencia, Integer> {

    Optional<Asistencia> findByEstudiante_IdEstudianteAndHorario_IdHorarioAndFechaAsistencia(
            Integer idEstudiante, Integer idHorario, LocalDate fechaAsistencia);

    List<Asistencia> findByHorario_IdHorarioAndFechaAsistencia(
            Integer idHorario, LocalDate fechaAsistencia);
}
