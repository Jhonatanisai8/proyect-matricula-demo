package com.isai.demowebregistrationsystem.repositorys;

import com.isai.demowebregistrationsystem.model.entities.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalonRepository
        extends JpaRepository<Salon, Integer> {

    Optional<Salon> findByCodigoSalon(String codigoSalon);

}
