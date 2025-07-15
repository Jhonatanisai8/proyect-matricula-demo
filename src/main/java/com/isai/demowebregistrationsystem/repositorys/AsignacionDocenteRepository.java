package com.isai.demowebregistrationsystem.repositorys;

import com.isai.demowebregistrationsystem.model.entities.AsignacionDocente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AsignacionDocenteRepository
        extends JpaRepository<AsignacionDocente, Integer> {

    Optional<AsignacionDocente> findByIdAsignacionAndDocente_IdDocente(Integer idAsignacion, Integer idDocente);

    List<AsignacionDocente> findByDocente_IdDocente(Integer idDocente);

    List<AsignacionDocente> findByDocente_IdDocenteAndPeriodoAcademico_Activo(Integer idDocente, boolean b);
}
