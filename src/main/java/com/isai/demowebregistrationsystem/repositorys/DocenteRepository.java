package com.isai.demowebregistrationsystem.repositorys;

import com.isai.demowebregistrationsystem.model.entities.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Integer> {


    boolean existsByCodigoDocente(String codigoDocente);

    boolean existsByEmailInstitucional(String emailInstitucional);

    Optional<Docente> findByPersonaIdPersona(Integer personaId);

}