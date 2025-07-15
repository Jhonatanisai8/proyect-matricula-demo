package com.isai.demowebregistrationsystem.services;

import com.isai.demowebregistrationsystem.model.dtos.SalonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SalonService {

    Page<SalonDTO> obtenerSalones(Pageable pageable);

    List<SalonDTO> obtenerSalones();

    Optional<SalonDTO> obtenerSalonPorId(Integer idSalon);

    SalonDTO guardarSalon(SalonDTO salonDTO);

    void eliminarSalon(Integer idSalon);


    void alternarEstadoDelSalón(Integer idSalon);

}
