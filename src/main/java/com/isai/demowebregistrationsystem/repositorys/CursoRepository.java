package com.isai.demowebregistrationsystem.repositorys;

import com.isai.demowebregistrationsystem.model.entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CursoRepository
        extends JpaRepository<Curso, Integer> {

    Optional<Curso> findByCodigoCurso(String codigoCurso);

    boolean existsByNombreCursoIgnoreCaseAndGradoIdGrado(String nombreCurso, Integer idGrado);

}
