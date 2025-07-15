package com.isai.demowebregistrationsystem.services.impl;

import com.isai.demowebregistrationsystem.model.dtos.GradoDTO;
import com.isai.demowebregistrationsystem.model.entities.Grado;
import com.isai.demowebregistrationsystem.repositorys.GradoRepository;
import com.isai.demowebregistrationsystem.services.GradoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GradoServiceImpl
        implements GradoService {

    private final GradoRepository gradoRepository;

    private Grado convertToEntity(GradoDTO gradoDTO) {
        Grado grado = new Grado();
        BeanUtils.copyProperties(gradoDTO, grado);
        return grado;
    }

    private GradoDTO convertToDto(Grado grado) {
        GradoDTO gradoDTO = new GradoDTO();
        BeanUtils.copyProperties(grado, gradoDTO);
        return gradoDTO;
    }


    @Transactional(readOnly = true)
    @Override
    public Page<GradoDTO> obtenerGrados(Pageable pageable) {
        return gradoRepository.findAll(pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<GradoDTO> obtenerGradoPorId(Integer id) {
        return gradoRepository.findById(id).map(this::convertToDto);
    }

    @Transactional
    @Override
    public GradoDTO guardarGrado(GradoDTO gradoDTO) {
        Optional<Grado> existingGradoByCodigo = gradoRepository.findByCodigoGrado(gradoDTO.getCodigoGrado());
        if (existingGradoByCodigo.isPresent() &&
                (gradoDTO.getIdGrado() == null || !existingGradoByCodigo.get().getIdGrado().equals(gradoDTO.getIdGrado()))) {
            throw new IllegalArgumentException("Ya existe un grado con el código '" + gradoDTO.getCodigoGrado() + "'.");
        }
        if (gradoRepository.existsByNombreGradoIgnoreCase(gradoDTO.getNombreGrado()) &&
                (gradoDTO.getIdGrado() == null || !gradoRepository.findByCodigoGrado(gradoDTO.getCodigoGrado()).map(Grado::getIdGrado).orElse(-1).equals(gradoDTO.getIdGrado()))) {
            boolean isNewOrDifferentGrado = (gradoDTO.getIdGrado() == null);
            if (!isNewOrDifferentGrado) {
                Optional<Grado> gradoWithSameName = gradoRepository.findAll().stream()
                        .filter(g -> g.getNombreGrado().equalsIgnoreCase(gradoDTO.getNombreGrado()) && !g.getIdGrado().equals(gradoDTO.getIdGrado()))
                        .findFirst();
                if (gradoWithSameName.isPresent()) {
                    throw new IllegalArgumentException("Ya existe un grado con el nombre '" + gradoDTO.getNombreGrado() + "'.");
                }
            } else {
                if (gradoRepository.existsByNombreGradoIgnoreCase(gradoDTO.getNombreGrado())) {
                    throw new IllegalArgumentException("Ya existe un grado con el nombre '" + gradoDTO.getNombreGrado() + "'.");
                }
            }
        }
        Grado grado;
        if (gradoDTO.getIdGrado() == null || gradoDTO.getIdGrado() == 0) {
            grado = new Grado();
            grado.setActivo(gradoDTO.getActivo() != null ? gradoDTO.getActivo() : true);
        } else {
            grado = gradoRepository.findById(gradoDTO.getIdGrado())
                    .orElseThrow(() -> new IllegalArgumentException("Grado con ID " + gradoDTO.getIdGrado() + " no encontrado."));
            grado.setActivo(gradoDTO.getActivo() != null ? gradoDTO.getActivo() : false);
        }
        BeanUtils.copyProperties(gradoDTO, grado, "idGrado", "activo");
        String codigoGrado = "GRAD00";
        Random random = new Random();
        codigoGrado = codigoGrado.concat(String.valueOf(random.nextInt(100)));
        System.out.println(gradoDTO.getCodigoGrado());
        System.out.println(gradoDTO.getIdGrado());
        if (gradoDTO.getIdGrado() == null) {
            grado.setCodigoGrado(codigoGrado);
        } else {
            grado.setCodigoGrado(gradoDTO.getCodigoGrado());
        }
        return convertToDto(gradoRepository.save(grado));
    }

    @Transactional
    @Override
    public void eliminarGrado(Integer id) {
        if (!gradoRepository.existsById(id)) {
            throw new IllegalArgumentException("El grado con ID " + id + " no existe.");
        }
        gradoRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void alternarEstadoGrado(Integer id) {
        Grado grado = gradoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Grado no encontrado con ID: " + id));
        grado.setActivo(!grado.getActivo());
        gradoRepository.save(grado);
    }

    @Transactional
    @Override
    public List<GradoDTO> obtenerTodosLosGradosActivos() {
        return gradoRepository.findAll().stream()
                .filter(Grado::getActivo)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
