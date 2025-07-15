package com.isai.demowebregistrationsystem.services.impl;

import com.isai.demowebregistrationsystem.model.dtos.SalonDTO;
import com.isai.demowebregistrationsystem.model.entities.Salon;
import com.isai.demowebregistrationsystem.repositorys.SalonRepository;
import com.isai.demowebregistrationsystem.services.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SalonServiceImpl
        implements SalonService {

    private final SalonRepository salonRepository;

    private Salon convertToEntity(SalonDTO salonDTO) {
        Salon salon = new Salon();
        BeanUtils.copyProperties(salonDTO, salon);
        return salon;
    }

    private SalonDTO convertToDto(Salon salon) {
        SalonDTO salonDTO = new SalonDTO();
        BeanUtils.copyProperties(salon, salonDTO);
        return salonDTO;
    }

    @Override
    public Page<SalonDTO> obtenerSalones(Pageable pageable) {
        return salonRepository.findAll(pageable).map(this::convertToDto);
    }

    @Override
    public List<SalonDTO> obtenerSalones() {
        return salonRepository.findAll()
                .stream()
                .filter(Salon::getActivo)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SalonDTO> obtenerSalonPorId(Integer idSalon) {
        return salonRepository.findById(idSalon).map(this::convertToDto);
    }

    @Override
    public SalonDTO guardarSalon(SalonDTO salonDTO) {
        if (salonDTO.getIdSalon() == null || salonDTO.getIdSalon() == 0) {
            if (salonRepository.findByCodigoSalon(salonDTO.getCodigoSalon()).isPresent()) {
                throw new IllegalArgumentException("Ya existe un salón con el código " + salonDTO.getCodigoSalon());
            }
        } else {
            Optional<Salon> existingSalonWithCode = salonRepository.findByCodigoSalon(salonDTO.getCodigoSalon());
            if (existingSalonWithCode.isPresent() && !existingSalonWithCode.get().getIdSalon().equals(salonDTO.getIdSalon())) {
                throw new IllegalArgumentException("Ya existe otro salón con el código " + salonDTO.getCodigoSalon());
            }
        }

        Salon salon = convertToEntity(salonDTO);

        if (salon.getIdSalon() == null || salon.getIdSalon() == 0) {
            salon.setActivo(true);
            salon.setTieneProyector(salonDTO.getTieneProyector() != null ? salonDTO.getTieneProyector() : false);
            salon.setTieneAireAcondicionado(salonDTO.getTieneAireAcondicionado() != null ? salonDTO.getTieneAireAcondicionado() : false);
        } else {


            Salon existingSalon = salonRepository.findById(salon.getIdSalon())
                    .orElseThrow(() -> new RuntimeException("Salón no encontrado para actualización."));

            BeanUtils.copyProperties(salonDTO, existingSalon);
            salon = existingSalon;

            if (salonDTO.getTieneProyector() == null) salon.setTieneProyector(false);
            if (salonDTO.getTieneAireAcondicionado() == null) salon.setTieneAireAcondicionado(false);
            if (salonDTO.getActivo() == null) salon.setActivo(false);
        }


        return convertToDto(salonRepository.save(salon));
    }

    @Override
    public void eliminarSalon(Integer idSalon) {
        if (!salonRepository.existsById(idSalon)) {
            throw new IllegalArgumentException("El salón con ID " + idSalon + " no existe.");
        }
        salonRepository.deleteById(idSalon);
    }

    @Override
    public void alternarEstadoDelSalón(Integer idSalon) {
        Salon salon = salonRepository.findById(idSalon)
                .orElseThrow(() -> new IllegalArgumentException("Salón no encontrado con ID: " + idSalon));
        salon.setActivo(!salon.getActivo());
        salonRepository.save(salon);
    }
}
