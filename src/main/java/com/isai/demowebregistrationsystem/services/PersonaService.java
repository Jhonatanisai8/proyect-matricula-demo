package com.isai.demowebregistrationsystem.services;

import com.isai.demowebregistrationsystem.model.entities.Persona;

import java.util.Optional;

public interface PersonaService {

    Persona guardarPersona(Persona persona);

    Optional<Persona> findById(Integer id);

    boolean existsByDni(String dni);
}
