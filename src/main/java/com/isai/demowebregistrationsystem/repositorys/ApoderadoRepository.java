package com.isai.demowebregistrationsystem.repositorys;

import com.isai.demowebregistrationsystem.model.entities.Apoderado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApoderadoRepository extends JpaRepository<Apoderado, Integer> {

    Optional<Apoderado> findByPersonaDni(String dni);

    Page<Apoderado> findByPersonaDniContainingIgnoreCase(String dni, Pageable pageable);

    Page<Apoderado> findAll(Pageable pageable);

    Optional<Apoderado> findByPersonaIdPersona(Integer idPersona);

}
