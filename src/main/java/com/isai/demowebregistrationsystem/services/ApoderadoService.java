package com.isai.demowebregistrationsystem.services;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.dtos.ApoderadoDTO;
import com.isai.demowebregistrationsystem.model.dtos.apoderado.DashboardApoderadpDTO;
import com.isai.demowebregistrationsystem.model.dtos.apoderado.EstudianteListaApoderadoDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.EstudianteRegistroDTO;
import com.isai.demowebregistrationsystem.model.dtos.registroInicioSesion.RegistroApoderadoDTO;
import com.isai.demowebregistrationsystem.model.entities.Apoderado;
import com.isai.demowebregistrationsystem.model.entities.Estudiante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ApoderadoService {

    Page<ApoderadoDTO> buscarApoderados(String dni, Pageable pageable);

    Optional<ApoderadoDTO> buscarApoderadoPorId(Integer idApoderado);

    ApoderadoDTO guardarApoderado(ApoderadoDTO apoderado);

    void eliminarApoderado(Integer idApoderado);

    Apoderado registrarNuevoApoderado(RegistroApoderadoDTO apoderadoDTO);

    /*METODOS PARA ROL DE APODERADO*/

    /**
     * Obtiene los datos necesarios para el dashboard del apoderado logueado.
     *
     * @param username El nombre de usuario del apoderado autenticado.
     * @return Un DTO con la información del dashboard.
     * @throws ResourceNotFoundException Si el apoderado no se encuentra.
     */
    DashboardApoderadpDTO obtenerDatosDashboardApoderado(String username) throws ResourceNotFoundException;

    /**
     * Registra un nuevo estudiante y lo vincula al apoderado autenticado.
     * Si la persona ya existe (por DNI), intenta vincular al estudiante a esa persona.
     *
     * @param estudianteRegistroDTO DTO con los datos de la persona y el estudiante.
     * @param usernameApoderado     El nombre de usuario del apoderado que realiza el registro.
     * @return El estudiante recién creado o vinculado.
     * @throws IllegalArgumentException  Si el DNI ya está registrado como estudiante o apoderado.
     * @throws ResourceNotFoundException Si el apoderado no se encuentra.
     */
    Estudiante registrarNuevoEstudiante(EstudianteRegistroDTO estudianteRegistroDTO, String usernameApoderado)
            throws IllegalArgumentException, ResourceNotFoundException;

    /**
     * Obtiene una lista de estudiantes vinculados al apoderado autenticado.
     *
     * @param usernameApoderado El nombre de usuario del apoderado.
     * @return Una lista de EstudianteListaDTO.
     * @throws ResourceNotFoundException Si el apoderado no se encuentra.
     */
    List<EstudianteListaApoderadoDTO> obtenerHijosDelApoderado(String usernameApoderado) throws ResourceNotFoundException;

}
