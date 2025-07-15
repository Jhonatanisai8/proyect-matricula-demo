package com.isai.demowebregistrationsystem.services.impl;

import com.isai.demowebregistrationsystem.model.entities.Persona;
import com.isai.demowebregistrationsystem.repositorys.PersonaRepository;
import com.isai.demowebregistrationsystem.services.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonaServiceImpl
        implements PersonaService {

    private final PersonaRepository personaRepository;

    private final AlmacenArchivoImpl almacenArchivo;

    @Override
    public Persona guardarPersona(Persona persona) {
        if (persona.getFechaRegistro() == null) {
            persona.setFechaRegistro(LocalDateTime.now());
        }
        persona.setFechaActualizacion(LocalDateTime.now());
        if (persona.getActivo() == null) {
            persona.setActivo(true);
        }
        String ruta = "";
        if (persona.getFoto() != null
                && !persona.getFoto().isEmpty()) {
            ruta = almacenArchivo.almacenarArchivo(persona.getFoto());
            persona.setFotoUrl(ruta);
        }
        return personaRepository.save(persona);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Persona> findById(Integer id) {
        return personaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByDni(String dni) {
        return personaRepository.existsByDni(dni);
    }
}