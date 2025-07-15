package com.isai.demowebregistrationsystem.services;


import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.dtos.docente.*;
import com.isai.demowebregistrationsystem.model.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DocenteService {

    /**
     * Registra un nuevo docente, creando o actualizando su información de Persona y su cuenta de Usuario.
     *
     * @param docenteRegistroDTO DTO con la información del docente y persona.
     * @return El DTO del docente registrado/actualizado con sus IDs.
     * @throws IllegalArgumentException Si el DNI, código de docente o username ya existen.
     */
    DocenteRegistroDTO registrarDocente(DocenteRegistroDTO docenteRegistroDTO);

    /**
     * Actualiza la información de un docente existente.
     *
     * @param idDocente          ID del docente a actualizar.
     * @param docenteRegistroDTO DTO con la nueva información.
     * @return El DTO del docente actualizado.
     * @throws ResourceNotFoundException Si el docente no es encontrado.
     * @throws IllegalArgumentException  Si el DNI, código de docente o username ya existen y pertenecen a otro.
     */
    DocenteRegistroDTO actualizarDocente(Integer idDocente, DocenteRegistroDTO docenteRegistroDTO);

    /**
     * Obtiene el detalle completo de un docente por su ID.
     *
     * @param idDocente ID del docente.
     * @return DTO con todos los detalles del docente.
     * @throws ResourceNotFoundException Si el docente no es encontrado.
     */
    DocenteDetalleDTO obtenerDetalleDocente(Integer idDocente);

    /**
     * Lista todos los docentes con paginación.
     *
     * @param pageable Objeto Pageable para paginación (número de página, tamaño, orden).
     * @return Una página de DTOs con la información de listado de docentes.
     */
    Page<DocenteDetalleDTO> listarDocentes(Pageable pageable);

    /**
     * Lista todos los docentes activos (o todos si no se filtra).
     *
     * @return Lista de todos los docentes (puede ser útil para selects en formularios).
     */
    List<DocenteDetalleDTO> listarTodosDocentes(); // Para el listado de selección en Asignacion

    /**
     * Desactiva un docente (marcarlo como inactivo en Persona y Usuario).
     *
     * @param idDocente ID del docente a desactivar.
     * @throws ResourceNotFoundException Si el docente no es encontrado.
     */
    void desactivarDocente(Integer idDocente);

    /**
     * Activa un docente (marcarlo como activo en Persona y Usuario).
     *
     * @param idDocente ID del docente a activar.
     * @throws ResourceNotFoundException Si el docente no es encontrado.
     */
    void activarDocente(Integer idDocente);

    /**
     * Asigna un curso a un docente.
     *
     * @param asignacionDocenteDTO DTO con los detalles de la asignación.
     * @return El objeto AsignacionDocente creado.
     * @throws IllegalArgumentException  Si la asignación ya existe o los IDs son inválidos.
     * @throws ResourceNotFoundException Si el docente, curso, grado o periodo no existen.
     */
    AsignacionDocente asignarCursoADocente(AsignacionDocenteDTO asignacionDocenteDTO);

    /**
     * Obtiene todas las asignaciones de un docente.
     *
     * @param idDocente ID del docente.
     * @return Lista de asignaciones.
     */
    List<AsignacionDocente> obtenerAsignacionesPorDocente(Integer idDocente);

    /**
     * Obtiene una asignación específica por su ID.
     *
     * @param idAsignacion ID de la asignación.
     * @return Optional con la asignación, o vacío si no se encuentra.
     */
    Optional<AsignacionDocente> obtenerAsignacionPorId(Integer idAsignacion);

    /**
     * Actualiza una asignación de docente existente.
     *
     * @param idAsignacion         ID de la asignación a actualizar.
     * @param asignacionDocenteDTO DTO con los nuevos datos.
     * @return La asignación actualizada.
     * @throws ResourceNotFoundException Si la asignación, docente, curso, grado o periodo no existen.
     */
    AsignacionDocente actualizarAsignacion(Integer idAsignacion, AsignacionDocenteDTO asignacionDocenteDTO);

    /**
     * Elimina lógicamente una asignación de docente (o físicamente si no es necesario mantener historial).
     *
     * @param idAsignacion ID de la asignación a eliminar.
     */
    void eliminarAsignacion(Integer idAsignacion);

    // Métodos para cargar listas de selección en los formularios
    List<Curso> listarTodosCursos();

    List<Grado> listarTodosGrados();

    List<PeriodoAcademico> listarTodosPeriodosAcademicos();

    //ROL DE DOCENTE
    Optional<DocentePerfilDTO> obtenerPerfilDocentePorUsername(String username);

    DocentePerfilDTO actualizarPerfilDocente(DocentePerfilDTO docentePerfilDTO);

    Optional<Docente> findByPersonaId(Integer personaId);

    List<CursoAsignadoDTO> listarCursosAsignadosPorUserName(String username);

    AsignacionDocenteDetalleDTO obtenerDetallesAsignacion(Integer idAsignacion, String username);

    List<CursoDocenteDTO> getCursosAsignadosAlDocente(String username);

    Page<EstudianteListaDTO> getEstudiantesPorAsignacion(String username, Integer idAsignacion, int page, int size);

    MisEstudiantesViewDTO getMisEstudiantesViewData(String username);

    List<CursoCalificacionDTO> getCursosAsignadosParaCalificacion(String username);

    RegistrarNotasViewDTO getRegistrarNotasViewData(String username);

    List<EstudianteNotaDTO> getEstudiantesConNotasActuales(String username, Integer idAsignacion);

    String registrarNotas(String username, RegistroNotasRequestDTO requestDTO);

    List<HorarioAsistenciaDTO> getHorariosParaAsistencia(String username);

    RegistrarAsistenciaViewDTO getRegistrarAsistenciaViewData(String username);

    List<EstudianteAsistenciaDTO> getEstudiantesConAsistenciaActual(String username, Integer idHorario, LocalDate fechaAsistencia);

    String registrarAsistencia(String username, RegistroAsistenciaRequestDTO requestDTO) throws ResourceNotFoundException;

}
