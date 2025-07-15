package com.isai.demowebregistrationsystem.services.impl;

import com.isai.demowebregistrationsystem.model.dtos.CursoDTO;
import com.isai.demowebregistrationsystem.model.entities.Curso;
import com.isai.demowebregistrationsystem.model.entities.Grado;
import com.isai.demowebregistrationsystem.repositorys.CursoRepository;
import com.isai.demowebregistrationsystem.repositorys.GradoRepository;
import com.isai.demowebregistrationsystem.services.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CursoServiceImpl
        implements CursoService {

    private final CursoRepository cursoRepository;

    private final GradoRepository gradoRepository;


    private Curso convertToEntity(CursoDTO cursoDTO) {
        Curso curso = new Curso();
        BeanUtils.copyProperties(cursoDTO, curso, "idGrado", "nombreGrado");

        if (cursoDTO.getIdGrado() != null) {
            Grado grado = gradoRepository.findById(cursoDTO.getIdGrado())
                    .orElseThrow(() -> new IllegalArgumentException("Grado con ID " + cursoDTO.getIdGrado() + " no encontrado."));
            curso.setGrado(grado);
        }
        return curso;
    }


    private CursoDTO convertToDto(Curso curso) {
        CursoDTO cursoDTO = new CursoDTO();
        BeanUtils.copyProperties(curso, cursoDTO);

        if (curso.getGrado() != null) {
            cursoDTO.setIdGrado(curso.getGrado().getIdGrado());
            cursoDTO.setNombreGrado(curso.getGrado().getNombreGrado());
        } else {
            cursoDTO.setNombreGrado("Grado No Asignado");
        }
        return cursoDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CursoDTO> obtenerCursos(Pageable pageable) {
        return cursoRepository.findAll(pageable).map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CursoDTO> obtenerCursoPorId(Integer id) {
        return cursoRepository.findById(id).map(this::convertToDto);
    }


    @Override
    public CursoDTO guardarCurso(CursoDTO cursoDTO) {
        // Validación de código_curso único
        Optional<Curso> existingCursoByCodigo = cursoRepository.findByCodigoCurso(cursoDTO.getCodigoCurso());
        if (existingCursoByCodigo.isPresent() &&
                (cursoDTO.getIdCurso() == null || !existingCursoByCodigo.get().getIdCurso().equals(cursoDTO.getIdCurso()))) {
            throw new IllegalArgumentException("Ya existe un curso con el código '" + cursoDTO.getCodigoCurso() + "'.");
        }

        // Validación de nombre_curso único por grado
        boolean isNameConflict = cursoRepository.existsByNombreCursoIgnoreCaseAndGradoIdGrado(
                cursoDTO.getNombreCurso(), cursoDTO.getIdGrado());

        if (isNameConflict) {
            //si es nuevo o editar
            if (cursoDTO.getIdCurso() == null) {
                throw new IllegalArgumentException("Ya existe un curso con el nombre '" + cursoDTO.getNombreCurso() + "' para el grado seleccionado.");
            } else {
                Optional<Curso> existingCourse = cursoRepository.findByCodigoCurso(cursoDTO.getCodigoCurso());
                if (existingCourse.isPresent() && !existingCourse.get().getIdCurso().equals(cursoDTO.getIdCurso())) {
                    throw new IllegalArgumentException("Ya existe un curso con el nombre '" + cursoDTO.getNombreCurso() + "' para el grado seleccionado.");
                }
            }
        }


        Curso curso;
        if (cursoDTO.getIdCurso() == null || cursoDTO.getIdCurso() == 0) { // Creando nuevo curso
            curso = new Curso();
            curso.setActivo(cursoDTO.getActivo() != null ? cursoDTO.getActivo() : true);
            curso.setEsObligatorio(cursoDTO.getEsObligatorio() != null ? cursoDTO.getEsObligatorio() : true);
        } else { // Editando curso existente
            curso = cursoRepository.findById(cursoDTO.getIdCurso())
                    .orElseThrow(() -> new IllegalArgumentException("Curso con ID " + cursoDTO.getIdCurso() + " no encontrado."));
            // Actualizar estado activo y obligatorio basado en el DTO
            curso.setActivo(cursoDTO.getActivo() != null ? cursoDTO.getActivo() : false);
            curso.setEsObligatorio(cursoDTO.getEsObligatorio() != null ? cursoDTO.getEsObligatorio() : false);
        }

        BeanUtils.copyProperties(cursoDTO, curso, "idCurso", "activo", "esObligatorio", "nombreGrado");

        // Setear el Grado asociado
        Grado grado = gradoRepository.findById(cursoDTO.getIdGrado())
                .orElseThrow(() -> new IllegalArgumentException("Grado con ID " + cursoDTO.getIdGrado() + " no encontrado."));
        curso.setGrado(grado);

        return convertToDto(cursoRepository.save(curso));
    }

    @Transactional(readOnly = true)
    @Override
    public void eliminarCurso(Integer id) {
        if (!cursoRepository.existsById(id)) {
            throw new IllegalArgumentException("El curso con ID " + id + " no existe.");
        }
        cursoRepository.deleteById(id);
    }

    @Override
    public void alternarEstadoCurso(Integer id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + id));
        curso.setActivo(!curso.getActivo());
        cursoRepository.save(curso);
    }
}
