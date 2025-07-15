package com.isai.demowebregistrationsystem.repositorys;

import com.isai.demowebregistrationsystem.model.entities.Seccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeccionRepository
        extends JpaRepository<Seccion, Integer> {

    List<Seccion> findByGradoIdGradoAndPeriodoAcademicoIdPeriodo(Integer idGrado, Integer idPeriodo);

}
