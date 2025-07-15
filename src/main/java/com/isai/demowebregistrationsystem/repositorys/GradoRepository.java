package com.isai.demowebregistrationsystem.repositorys;

import com.isai.demowebregistrationsystem.model.entities.Grado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradoRepository
        extends JpaRepository<Grado, Integer> {

    Optional<Grado> findByCodigoGrado(String codigoGrado);

    boolean existsByNombreGradoIgnoreCase(String nombreGrado);

    List<Grado> findByActivoTrueOrderByOrdenAsc();

}
