package com.isai.demowebregistrationsystem.services.impl;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.exceptions.ValidationException;
import com.isai.demowebregistrationsystem.model.constantes.HorarioConstantes;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.EstudianteDetalleDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.EstudianteListadoDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.EstudianteRegistroDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante.*;
import com.isai.demowebregistrationsystem.model.dtos.opciones.ApoderadoOptionDTO;
import com.isai.demowebregistrationsystem.model.dtos.opciones.GradoOptionDTO;
import com.isai.demowebregistrationsystem.model.entities.*;
import com.isai.demowebregistrationsystem.model.enums.Rol;
import com.isai.demowebregistrationsystem.repositorys.*;
import com.isai.demowebregistrationsystem.services.EstudianteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl
        implements EstudianteService {

    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final PersonaRepository personaRepository;
    private final AlmacenArchivoImpl almacenArchivoImpl;
    private final ApoderadoRepository apoderadoRepository;
    private final GradoRepository gradoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final HorarioRepository horarioRepository;
    private final CalificacionRepository calificacionRepository;
    private final PeriodoAcademicoRepository periodoAcademicoRepository;


    @Override
    @Transactional(readOnly = true)
    public Page<EstudianteListadoDTO> listarEstudiantesPaginado(String keyword, Boolean activo, Pageable pageable) {
        Specification<Estudiante> spec = Specification.where(null);
        if (keyword != null && !keyword.trim().isEmpty()) {
            String lowerCaseKeyword = keyword.toLowerCase();
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("codigoEstudiante")), "%" + lowerCaseKeyword + "%"),
                    cb.like(cb.lower(root.get("persona").get("dni")), "%" + lowerCaseKeyword + "%"),
                    cb.like(cb.lower(root.get("persona").get("nombres")), "%" + lowerCaseKeyword + "%"),
                    cb.like(cb.lower(root.get("persona").get("apellidos")), "%" + lowerCaseKeyword + "%")
            ));
        }

        if (activo != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("persona").get("activo"), activo));
        }

        return estudianteRepository.findAll(spec, pageable)
                .map(this::convertirAEstudianteListadoDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteDetalleDTO obtenerEstudianteDetalle(Integer idEstudiante) {
        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + idEstudiante));
        return convertirAEstudianteDetalleDTO(estudiante);
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteRegistroDTO obtenerEstudianteParaEdicion(Integer idEstudiante) {
        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + idEstudiante));
        return convertirAEstudianteRegistroDTO(estudiante);
    }


    @Override
    @Transactional
    public void guardarEstudiante(EstudianteRegistroDTO dto) {

        Optional<Persona> existingPersonaByDni = personaRepository.findByDni(dto.getDni());
        if (existingPersonaByDni.isPresent() && !existingPersonaByDni.get().getIdPersona().equals(dto.getIdPersona())) {
            throw new ValidationException("Ya existe una persona con el DNI: " + dto.getDni());
        }

        if (dto.getCodigoEstudiante() != null && !dto.getCodigoEstudiante().isEmpty()) {
            Optional<Estudiante> existingEstudianteByCodigo = estudianteRepository.findByCodigoEstudiante(dto.getCodigoEstudiante());
            if (existingEstudianteByCodigo.isPresent() && !existingEstudianteByCodigo.get().getIdEstudiante().equals(dto.getIdEstudiante())) {
                throw new ValidationException("Ya existe un estudiante con el código: " + dto.getCodigoEstudiante());
            }
        }

        Persona persona;
        Estudiante estudiante;

        if (dto.getIdPersona() != null) {
            persona = personaRepository.findById(dto.getIdPersona())
                    .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada para el ID: " + dto.getIdPersona()));
            estudiante = estudianteRepository.findById(dto.getIdEstudiante())
                    .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado para el ID: " + dto.getIdEstudiante()));
        } else {
            persona = new Persona();
            estudiante = new Estudiante();
            persona.setFechaRegistro(LocalDateTime.now());
            if (dto.getCodigoEstudiante() == null || dto.getCodigoEstudiante().isEmpty()) {
                estudiante.setCodigoEstudiante(generarCodigoEstudiante());
            }
        }


        persona.setNombres(dto.getNombres());
        persona.setApellidos(dto.getApellidos());
        persona.setDni(dto.getDni());
        persona.setDireccion(dto.getDireccion());
        persona.setEmailPersonal(dto.getEmailPersonal());
        persona.setEstadoCivil(dto.getEstadoCivil());
        persona.setFechaNacimiento(dto.getFechaNacimiento());

        persona.setGenero(dto.getGenero());
        persona.setTelefono(dto.getTelefono());
        persona.setTipoDocumento(dto.getTipoDocumento());
        persona.setActivo(dto.getPersonaActivo());
        persona.setFechaActualizacion(LocalDateTime.now());

        String ruta = "";
        if (dto.getFoto() != null && !dto.getFoto().isEmpty() && dto.getFoto().getSize() > 0) {
            ruta = almacenArchivoImpl.almacenarArchivo(dto.getFoto());
            persona.setFotoUrl(ruta);
        }


        personaRepository.save(persona);


        estudiante.setPersona(persona);
        estudiante.setCodigoEstudiante(dto.getCodigoEstudiante() != null && !dto.getCodigoEstudiante().isEmpty() ? dto.getCodigoEstudiante() : estudiante.getCodigoEstudiante());
        estudiante.setEmailEducativo(dto.getEmailEducativo());
        estudiante.setInstitucionProcedencia(dto.getInstitucionProcedencia());
        estudiante.setGradoAnterior(dto.getGradoAnterior());
        estudiante.setSeguroEscolar(dto.getSeguroEscolar());
        estudiante.setAlergias(dto.getAlergias());
        estudiante.setObservacionesMedicas(dto.getObservacionesMedicas());
        estudiante.setContactoEmergencia(dto.getContactoEmergencia());
        estudiante.setTelefonoEmergencia(dto.getTelefonoEmergencia());
        estudiante.setTipoSangre(dto.getTipoSangre());

        if (dto.getIdApoderado() != null) {
            Apoderado apoderado = apoderadoRepository.findById(dto.getIdApoderado())
                    .orElseThrow(() -> new ResourceNotFoundException("Apoderado no encontrado con ID: " + dto.getIdApoderado()));
            estudiante.setApoderadoPrincipal(apoderado);
        } else {
            estudiante.setApoderadoPrincipal(null);
        }

        estudianteRepository.save(estudiante);


        Usuario usuario;

        Optional<Usuario> existingUserByPersonId = usuarioRepository.findByPersonaIdPersona(persona.getIdPersona());

        if (existingUserByPersonId.isPresent()) {

            usuario = existingUserByPersonId.get();


            if (dto.getIdUsuario() != null && !dto.getIdUsuario().equals(usuario.getIdUsuario())) {
                throw new ValidationException("La persona ya está asociada a otro usuario. ID de persona: " + persona.getIdPersona());
            }


            String newUsername = dto.getNombres().substring(0, Math.min(dto.getNombres().length(), 5)).concat(dto.getApellidos().substring(0, Math.min(dto.getApellidos().length(), 2))).toLowerCase();
            if (!usuario.getUserName().equals(newUsername)) {

                Optional<Usuario> userWithNewName = usuarioRepository.findByUserName(newUsername);
                if (userWithNewName.isPresent() && !userWithNewName.get().getIdUsuario().equals(usuario.getIdUsuario())) {
                    throw new ValidationException("El nombre de usuario '" + newUsername + "' ya está en uso por otro usuario.");
                }
                usuario.setUserName(newUsername);
            }


            usuario.setPasswordHash(passwordEncoder.encode(dto.getEmailPersonal()));
            usuario.setFechaCreacion(LocalDateTime.now());

        } else {

            usuario = new Usuario();
            usuario.setFechaCreacion(LocalDateTime.now());
            usuario.setActivo(true);
            //usuario.setIntentosFallidos(0);
            usuario.setRol(Rol.ESTUDIANTE);
            usuario.setPersona(persona);


            String newUsername = dto.getNombres().substring(0, Math.min(dto.getNombres().length(), 5)).concat(dto.getApellidos().substring(0, Math.min(dto.getApellidos().length(), 2))).toLowerCase();
            if (usuarioRepository.existsByUserName(newUsername)) {


                throw new ValidationException("El nombre de usuario generado ('" + newUsername + "') ya está en uso. Por favor, intente con nombres diferentes.");
            }
            usuario.setUserName(newUsername);
            usuario.setPasswordHash(passwordEncoder.encode(dto.getEmailPersonal()));
        }

        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminarEstudiante(Integer idEstudiante) {
        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + idEstudiante));
        Persona persona = estudiante.getPersona();
        persona.setActivo(false);
        persona.setFechaActualizacion(LocalDateTime.now());
        personaRepository.save(persona);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApoderadoOptionDTO> obtenerApoderadosDisponibles() {
        return apoderadoRepository.findAll().stream()
                .map(apoderado -> new ApoderadoOptionDTO(
                        apoderado.getIdApoderado(),
                        apoderado.getPersona().getNombres() + " " + apoderado.getPersona().getApellidos() + " (" + apoderado.getPersona().getDni() + ")"
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradoOptionDTO> obtenerGradosDisponibles() {
        return gradoRepository.findByActivoTrueOrderByOrdenAsc().stream()
                .map(grado -> new GradoOptionDTO(grado.getIdGrado(), grado.getNombreGrado()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getEstadosCiviles() {
        Map<String, String> estados = new LinkedHashMap<>();
        estados.put("SOLTERO(A)", "Soltero(a)");
        return estados;
    }

    @Override
    public Map<String, String> getGeneros() {
        Map<String, String> generos = new LinkedHashMap<>();
        generos.put("MASCULINO", "Masculino");
        generos.put("FEMENINO", "Femenino");
        return generos;
    }

    @Override
    public Map<String, String> getTiposDocumento() {
        Map<String, String> tipos = new LinkedHashMap<>();
        tipos.put("DNI", "DNI");
        tipos.put("CARNET_EXTRANJERIA", "Carnet de Extranjería");
        tipos.put("PASAPORTE", "Pasaporte");
        return tipos;
    }

    @Override
    public Map<String, String> getTiposSangre() {
        Map<String, String> tipos = new LinkedHashMap<>();
        tipos.put("A_POSITIVO", "A+");
        tipos.put("A_NEGATIVO", "A-");
        tipos.put("B_POSITIVO", "B+");
        tipos.put("B_NEGATIVO", "B-");
        tipos.put("AB_POSITIVO", "AB+");
        tipos.put("AB_NEGATIVO", "AB-");
        tipos.put("O_POSITIVO", "O+");
        tipos.put("O_NEGATIVO", "O-");
        return tipos;
    }

    @Override
    public Map<String, String> getGradosAnteriores() {
        Map<String, String> grados = new LinkedHashMap<>();
        grados.put("INICIAL_3", "Inicial 3 años");
        grados.put("INICIAL_4", "Inicial 4 años");
        grados.put("INICIAL_5", "Inicial 5 años");
        grados.put("PRIMARIA_1", "1° Primaria");
        grados.put("PRIMARIA_2", "2° Primaria");
        grados.put("PRIMARIA_3", "3° Primaria");
        grados.put("PRIMARIA_4", "4° Primaria");
        grados.put("PRIMARIA_5", "5° Primaria");
        grados.put("PRIMARIA_6", "6° Primaria");
        return grados;
    }

    private EstudianteListadoDTO convertirAEstudianteListadoDTO(Estudiante estudiante) {
        EstudianteListadoDTO dto = new EstudianteListadoDTO();
        dto.setIdEstudiante(estudiante.getIdEstudiante());
        dto.setIdPersona(estudiante.getPersona().getIdPersona());
        dto.setCodigoEstudiante(estudiante.getCodigoEstudiante());
        dto.setNombresCompletos(estudiante.getPersona().getNombres() + " " + estudiante.getPersona().getApellidos());
        dto.setDni(estudiante.getPersona().getDni());
        dto.setEmailPersonal(estudiante.getPersona().getEmailPersonal());
        dto.setTelefono(estudiante.getPersona().getTelefono());
        dto.setFechaNacimiento(estudiante.getPersona().getFechaNacimiento());
        dto.setActivo(estudiante.getPersona().getActivo());

        matriculaRepository.findTopByEstudiante_IdEstudianteOrderByFechaMatriculaDesc(estudiante.getIdEstudiante())
                .ifPresent(matricula -> {
                    dto.setGradoActual(matricula.getGrado().getNombreGrado());
                    dto.setSeccionActual(matricula.getSeccion().getNombreSeccion());
                });
        return dto;
    }

    private EstudianteDetalleDTO convertirAEstudianteDetalleDTO(Estudiante estudiante) {
        EstudianteDetalleDTO dto = new EstudianteDetalleDTO();
        Persona persona = estudiante.getPersona();


        dto.setIdPersona(persona.getIdPersona());
        dto.setNombres(persona.getNombres());
        dto.setApellidos(persona.getApellidos());
        dto.setDni(persona.getDni());
        dto.setDireccion(persona.getDireccion());
        dto.setEmailPersonal(persona.getEmailPersonal());
        dto.setEstadoCivil(persona.getEstadoCivil());
        dto.setFechaNacimiento(persona.getFechaNacimiento());
        dto.setFotoUrl(persona.getFotoUrl());
        dto.setGenero(persona.getGenero());
        dto.setTelefono(persona.getTelefono());
        dto.setTipoDocumento(persona.getTipoDocumento());
        dto.setPersonaActivo(persona.getActivo());
        dto.setFechaRegistroPersona(persona.getFechaRegistro());
        dto.setFechaActualizacionPersona(persona.getFechaActualizacion());


        dto.setIdEstudiante(estudiante.getIdEstudiante());
        dto.setCodigoEstudiante(estudiante.getCodigoEstudiante());
        dto.setEmailEducativo(estudiante.getEmailEducativo());
        dto.setInstitucionProcedencia(estudiante.getInstitucionProcedencia());
        dto.setGradoAnterior(estudiante.getGradoAnterior());
        dto.setSeguroEscolar(estudiante.getSeguroEscolar());
        dto.setAlergias(estudiante.getAlergias());
        dto.setObservacionesMedicas(estudiante.getObservacionesMedicas());
        dto.setContactoEmergencia(estudiante.getContactoEmergencia());
        dto.setTelefonoEmergencia(estudiante.getTelefonoEmergencia());
        dto.setTipoSangre(estudiante.getTipoSangre());


        if (estudiante.getApoderadoPrincipal() != null) {
            Apoderado apoderado = estudiante.getApoderadoPrincipal();
            Persona apoderadoPersona = apoderado.getPersona();
            dto.setIdApoderadoPrincipal(apoderado.getIdApoderado());
            dto.setApoderadoNombresCompletos(apoderadoPersona.getNombres() + " " + apoderadoPersona.getApellidos());
            dto.setApoderadoParentesco(apoderado.getParentesco());
            dto.setApoderadoTelefonoContacto(apoderadoPersona.getTelefono());
            dto.setApoderadoEmailPersonal(apoderadoPersona.getEmailPersonal());
        }


        matriculaRepository.findTopByEstudiante_IdEstudianteOrderByFechaMatriculaDesc(estudiante.getIdEstudiante())
                .ifPresent(matricula -> {
                    dto.setIdMatriculaActual(matricula.getIdMatricula());
                    dto.setNumeroMatriculaActual(matricula.getNumeroMatricula());
                    dto.setEstadoMatriculaActual(matricula.getEstadoMatricula());
                    dto.setFechaMatriculaActual(matricula.getFechaMatricula());
                    dto.setGradoMatriculado(matricula.getGrado().getNombreGrado());
                    dto.setSeccionMatriculada(matricula.getSeccion().getNombreSeccion());
                    dto.setPeriodoAcademicoMatriculado(matricula.getPeriodoAcademico().getNombrePeriodo() + " (" + matricula.getPeriodoAcademico().getAnoAcademico() + ")");
                });

        return dto;
    }

    private EstudianteRegistroDTO convertirAEstudianteRegistroDTO(Estudiante estudiante) {
        EstudianteRegistroDTO dto = new EstudianteRegistroDTO();
        Persona persona = estudiante.getPersona();

        dto.setIdEstudiante(estudiante.getIdEstudiante());
        dto.setIdPersona(persona.getIdPersona());


        dto.setNombres(persona.getNombres());
        dto.setApellidos(persona.getApellidos());
        dto.setDni(persona.getDni());
        dto.setDireccion(persona.getDireccion());
        dto.setEmailPersonal(persona.getEmailPersonal());
        dto.setEstadoCivil(persona.getEstadoCivil());
        dto.setFechaNacimiento(persona.getFechaNacimiento());
        dto.setFotoUrl(persona.getFotoUrl());
        dto.setGenero(persona.getGenero());
        dto.setTelefono(persona.getTelefono());
        dto.setTipoDocumento(persona.getTipoDocumento());
        dto.setPersonaActivo(persona.getActivo());


        dto.setCodigoEstudiante(estudiante.getCodigoEstudiante());
        dto.setEmailEducativo(estudiante.getEmailEducativo());
        dto.setInstitucionProcedencia(estudiante.getInstitucionProcedencia());
        dto.setGradoAnterior(estudiante.getGradoAnterior());
        dto.setSeguroEscolar(estudiante.getSeguroEscolar());
        dto.setAlergias(estudiante.getAlergias());
        dto.setObservacionesMedicas(estudiante.getObservacionesMedicas());
        dto.setContactoEmergencia(estudiante.getContactoEmergencia());
        dto.setTelefonoEmergencia(estudiante.getTelefonoEmergencia());
        dto.setTipoSangre(estudiante.getTipoSangre());


        if (estudiante.getApoderadoPrincipal() != null) {
            dto.setIdApoderado(estudiante.getApoderadoPrincipal().getIdApoderado());
        }

        return dto;
    }

    private String generarCodigoEstudiante() {
        return "EST" + System.currentTimeMillis();
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteDashboardDTO obtenerDatosDashboardEstudiante(String username) throws ResourceNotFoundException {
        Usuario usuario = usuarioRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));

        Estudiante estudiante = estudianteRepository.findByPersonaIdPersona(usuario.getPersona().getIdPersona())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado para el usuario: " + username));


        // obtenemos la matrícula activa del estudiante
        Matricula matriculaActual = matriculaRepository
                .findByEstudiante_IdEstudianteAndPeriodoAcademico_ActivoTrueAndEstadoMatriculaOrderByFechaMatriculaDesc(
                        estudiante.getIdEstudiante(), "ACTIVA")
                .orElse(null);

        String gradoActual = "N/A";
        String seccionActual = "N/A";
        String periodoAcademicoActual = "N/A";
        String numeroMatriculaActual = "N/A";

        if (matriculaActual != null) {
            gradoActual = matriculaActual.getGrado().getNombreGrado();
            seccionActual = matriculaActual.getSeccion().getNombreSeccion();
            periodoAcademicoActual = matriculaActual.getPeriodoAcademico().getNombrePeriodo() + " " + matriculaActual.getPeriodoAcademico().getAnoAcademico();
            numeroMatriculaActual = matriculaActual.getNumeroMatricula();
        }

        String apoderadoPrincipalNombre = "N/A";
        String apoderadoPrincipalTelefono = "N/A";

        // obtenemos el apoderado principal si existe
        if (estudiante.getApoderadoPrincipal() != null) {
            Apoderado apoderado = estudiante.getApoderadoPrincipal();
            apoderadoPrincipalNombre = apoderado.getPersona().getNombres() + " " + apoderado.getPersona().getApellidos();
            apoderadoPrincipalTelefono = apoderado.getPersona().getTelefono();
            if (apoderadoPrincipalTelefono == null || apoderadoPrincipalTelefono.isEmpty()) {
                apoderadoPrincipalTelefono = apoderado.getTelefonoTrabajo();
            }
        }


        return EstudianteDashboardDTO.builder()
                .nombresCompletos(estudiante.getPersona().getNombres() + " " + estudiante.getPersona().getApellidos())
                .codigoEstudiante(estudiante.getCodigoEstudiante())
                .emailEducativo(estudiante.getEmailEducativo())
                .fechaNacimiento(estudiante.getPersona().getFechaNacimiento())
                .fotoUrl(estudiante.getPersona().getFotoUrl())
                .gradoActual(gradoActual)
                .seccionActual(seccionActual)
                .periodoAcademicoActual(periodoAcademicoActual)
                .numeroMatriculaActual(numeroMatriculaActual)
                .contactoEmergencia(estudiante.getContactoEmergencia())
                .telefonoEmergencia(estudiante.getTelefonoEmergencia())
                .tipoSangre(estudiante.getTipoSangre())
                .seguroEscolar(estudiante.getSeguroEscolar())
                .apoderadoPrincipal(apoderadoPrincipalNombre)
                .telefonoApoderadoPrincipal(apoderadoPrincipalTelefono)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CursosEstudianteViewDTO obtenerMisCursos(String username) throws ResourceNotFoundException {
        Usuario usuario = usuarioRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));

        Estudiante estudiante = estudianteRepository.findByPersonaIdPersona(usuario.getPersona().getIdPersona())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado para el usuario: " + username));

        /*Obtenemos la matricula del estudiante*/
        Matricula matriculaActual = matriculaRepository
                .findByEstudiante_IdEstudianteAndPeriodoAcademico_ActivoTrueAndEstadoMatriculaOrderByFechaMatriculaDesc(
                        estudiante.getIdEstudiante(), "ACTIVA")
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró una matrícula activa para el estudiante " + username + " en el período actual."));

        /*Preparamos la lista de cursos*/
        List<CursoEstudianteDTO> cursosMatriculadosDTO = new ArrayList<>();

        // Los cursos no están directamente en Matricula.
        // Los cursos se asocian a un Grado y un Período Académico a través de Asignacion_Docente y Horario.
        // Para el estudiante, sus cursos son los cursos del Grado y Sección en los que está matriculado
        // y que tienen horarios asignados para su sección y período actual.

        Integer idGradoMatriculado = matriculaActual.getGrado().getIdGrado();
        Integer idSeccionMatriculada = matriculaActual.getSeccion().getIdSeccion();
        Integer idPeriodoAcademicoMatriculado = matriculaActual.getPeriodoAcademico().getIdPeriodo();

        /*Buscamos todos los horarios para el grado, seccion y periodo academico del estudiante*/
        List<Horario> horariosDelEstudiante = horarioRepository.findByGrado_IdGradoAndSeccion_IdSeccionAndPeriodoAcademico_IdPeriodoAndActivoTrue(
                idGradoMatriculado, idSeccionMatriculada, idPeriodoAcademicoMatriculado);

        /*Agrupamos los horarios por el curso para evitar duplicados si un curso tiene multiples horarios y cremos un dto por cada curso unico*/
        horariosDelEstudiante.stream()
                .map(Horario::getCurso)
                .distinct()
                .forEach(curso -> {
                    /*para cada curso encontrarmos un horario representativo*/
                    Optional<Horario> horarioPrincipal = horariosDelEstudiante.stream()
                            .filter(h -> h.getCurso().equals(curso))
                            .findFirst(); // O alguna lógica para elegir el "principal"

                    String nombreDocente = "N/A";
                    String diaSemana = "N/A";
                    LocalTime horaInicio = null;
                    LocalTime horaFin = null;
                    String nombreSalon = "N/A";

                    if (horarioPrincipal.isPresent()) {
                        Horario h = horarioPrincipal.get();
                        if (h.getDocente() != null && h.getDocente().getPersona() != null) {
                            nombreDocente = h.getDocente().getPersona().getNombres() + " " + h.getDocente().getPersona().getApellidos();
                        }
                        diaSemana = h.getDiaSemana();
                        horaInicio = h.getHoraInicio();
                        horaFin = h.getHoraFin();
                        if (h.getSalon() != null) {
                            nombreSalon = h.getSalon().getNombreSalon();
                        }
                    }

                    cursosMatriculadosDTO.add(CursoEstudianteDTO.builder()
                            .idCurso(curso.getIdCurso())
                            .nombreCurso(curso.getNombreCurso())
                            .codigoCurso(curso.getCodigoCurso())
                            .areaConocimiento(curso.getAreaConocimiento())
                            .descripcion(curso.getDescripcion())
                            .creditos(curso.getCreditos())
                            .horasSemanales(curso.getHorasSemanales())
                            .nombreDocente(nombreDocente)
                            .diaSemana(diaSemana)
                            .horaInicio(horaInicio)
                            .horaFin(horaFin)
                            .nombreSalon(nombreSalon)
                            .build());
                });

        String mensaje = "Aún no se han asignado cursos para tu matrícula actual.";
        if (!cursosMatriculadosDTO.isEmpty()) {
            mensaje = null;
        }

        return CursosEstudianteViewDTO.builder()
                .nombreEstudiante(estudiante.getPersona().getNombres() + " " + estudiante.getPersona().getApellidos())
                .gradoActual(matriculaActual.getGrado().getNombreGrado())
                .seccionActual(matriculaActual.getSeccion().getNombreSeccion())
                .periodoAcademicoActual(matriculaActual.getPeriodoAcademico().getNombrePeriodo() + " " + matriculaActual.getPeriodoAcademico().getAnoAcademico())
                .cursosMatriculados(cursosMatriculadosDTO.stream()
                        .sorted((c1, c2) -> c1.getNombreCurso().compareToIgnoreCase(c2.getNombreCurso()))
                        .collect(Collectors.toList()))
                .mensajeSinCursos(mensaje)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public NotasEstudianteViewDTO obtenerMisNotas(String username) throws ResourceNotFoundException {
        Usuario usuario = usuarioRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));

        Estudiante estudiante = estudianteRepository.findByPersonaIdPersona(usuario.getPersona().getIdPersona())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado para el usuario: " + username));

        Matricula matriculaActual = matriculaRepository
                .findByEstudiante_IdEstudianteAndPeriodoAcademico_ActivoTrueAndEstadoMatriculaOrderByFechaMatriculaDesc(
                        estudiante.getIdEstudiante(), "ACTIVA")
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró una matrícula activa para el estudiante " + username + " en el período actual para ver notas."));

        Integer idEstudiante = estudiante.getIdEstudiante();
        Integer idPeriodoAcademico = matriculaActual.getPeriodoAcademico().getIdPeriodo();

        /*obtenemos todas las calificaciones del estudiante para el periodo actual*/
        List<Calificacion> calificaciones = calificacionRepository
                .findByEstudiante_IdEstudianteAndPeriodoAcademico_IdPeriodo(idEstudiante, idPeriodoAcademico);

        /*agrupamos las notas por cursos*/
        Map<Curso, List<Calificacion>> calificacionesPorCurso = calificaciones.stream()
                .collect(Collectors.groupingBy(Calificacion::getCurso));

        List<CursoNotasEstudianteDTO> cursosConNotasDTO = new ArrayList<>();

        for (Map.Entry<Curso, List<Calificacion>> entry : calificacionesPorCurso.entrySet()) {
            Curso curso = entry.getKey();
            List<Calificacion> notasDelCurso = entry.getValue();
            /*
            buscamos el docente principal para el curso en periodo y seccion*/
            String nombreDocente = "N/A";
            List<Horario> horariosCurso = horarioRepository
                    .findByCurso_IdCursoAndGrado_IdGradoAndSeccion_IdSeccionAndPeriodoAcademico_IdPeriodoAndActivoTrue(
                            curso.getIdCurso(), matriculaActual.getGrado().getIdGrado(),
                            matriculaActual.getSeccion().getIdSeccion(), idPeriodoAcademico);

            if (!horariosCurso.isEmpty()) {
                Horario horarioPrincipal = horariosCurso.get(0);
                if (horarioPrincipal.getDocente() != null && horarioPrincipal.getDocente().getPersona() != null) {
                    nombreDocente = horarioPrincipal.getDocente().getPersona().getNombres() + " " +
                            horarioPrincipal.getDocente().getPersona().getApellidos();
                }
            }


            List<CalificacionEstudianteDTO> calificacionesDTO = notasDelCurso.stream()
                    .map(c -> CalificacionEstudianteDTO.builder()
                            .idCalificacion(c.getIdCalificacion())
                            .nombreCurso(c.getCurso().getNombreCurso())
                            .tipoEvaluacion(c.getTipoEvaluacion())
                            .nota(c.getNota())
                            .pesoPorcentual(c.getPesoPorcentual())
                            .fechaEvaluacion(c.getFechaEvaluacion())
                            .observaciones(c.getObservaciones())
                            .build())
                    .sorted(Comparator.comparing(CalificacionEstudianteDTO::getFechaEvaluacion).reversed()) // Ordenar por fecha descendente
                    .collect(Collectors.toList());

            // calculamos el promedio
            BigDecimal promedioCurso;
            if (calificacionesDTO.stream().anyMatch(c -> c.getPesoPorcentual() != null)) {
                BigDecimal sumaPesos = BigDecimal.ZERO;
                BigDecimal sumaNotasPonderadas = BigDecimal.ZERO;

                for (CalificacionEstudianteDTO c : calificacionesDTO) {
                    if (c.getPesoPorcentual() != null) {
                        BigDecimal peso = c.getPesoPorcentual().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                        sumaNotasPonderadas = sumaNotasPonderadas.add(c.getNota().multiply(peso));
                        sumaPesos = sumaPesos.add(peso);
                    }
                }
                if (sumaPesos.compareTo(BigDecimal.ZERO) > 0) {
                    promedioCurso = sumaNotasPonderadas.divide(sumaPesos, 2, RoundingMode.HALF_UP);
                } else {
                    promedioCurso = BigDecimal.ZERO;
                }
            } else {
                /*calculo promedio simple si no hay pesos o si los pesos son nulos*/
                Optional<BigDecimal> sumaNotas = calificacionesDTO.stream()
                        .map(CalificacionEstudianteDTO::getNota)
                        .reduce(BigDecimal::add);

                if (sumaNotas.isPresent() && !calificacionesDTO.isEmpty()) {
                    promedioCurso = sumaNotas.get().divide(BigDecimal.valueOf(calificacionesDTO.size()), 2, RoundingMode.HALF_UP);
                } else {
                    promedioCurso = BigDecimal.ZERO;
                }
            }


            cursosConNotasDTO.add(CursoNotasEstudianteDTO.builder()
                    .idCurso(curso.getIdCurso())
                    .nombreCurso(curso.getNombreCurso())
                    .codigoCurso(curso.getCodigoCurso())
                    .nombreDocente(nombreDocente)
                    .calificaciones(calificacionesDTO)
                    .promedioCurso(promedioCurso)
                    .build());
        }

        String mensaje = "Aún no hay calificaciones registradas para tu matrícula actual.";
        if (!cursosConNotasDTO.isEmpty()) {
            mensaje = null;
        }

        return NotasEstudianteViewDTO.builder()
                .nombreEstudiante(estudiante.getPersona().getNombres() + " " + estudiante.getPersona().getApellidos())
                .gradoActual(matriculaActual.getGrado().getNombreGrado())
                .seccionActual(matriculaActual.getSeccion().getNombreSeccion())
                .periodoAcademicoActual(matriculaActual.getPeriodoAcademico().getNombrePeriodo() + " " + matriculaActual.getPeriodoAcademico().getAnoAcademico())
                .cursosConNotas(cursosConNotasDTO.stream()
                        .sorted(Comparator.comparing(CursoNotasEstudianteDTO::getNombreCurso))
                        .collect(Collectors.toList()))
                .mensajeSinNotas(mensaje)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HorarioEstudianteViewDTO obtenerMiHorario(String username) throws ResourceNotFoundException {

        Usuario usuario = usuarioRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));

        Estudiante estudiante = estudianteRepository.findByPersonaIdPersona(usuario.getPersona().getIdPersona())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado para el usuario: " + username));

        PeriodoAcademico periodoActual = periodoAcademicoRepository.findByEstado("PENDIENTE")
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró un período académico activo."));

        Matricula matriculaActiva = matriculaRepository
                .findByEstudiante_IdEstudianteAndPeriodoAcademico_IdPeriodoAndEstadoMatricula(
                        estudiante.getIdEstudiante(),
                        periodoActual.getIdPeriodo(),
                        "ACTIVA"
                )
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró una matrícula activa para el estudiante en el período actual."));

        List<Horario> horariosDB = horarioRepository
                .findByGrado_IdGradoAndSeccion_IdSeccionAndPeriodoAcademico_IdPeriodoAndActivoTrue(
                        matriculaActiva.getGrado().getIdGrado(),
                        matriculaActiva.getSeccion().getIdSeccion(),
                        matriculaActiva.getPeriodoAcademico().getIdPeriodo()
                );

        HorarioEstudianteViewDTO horarioViewDTO = new HorarioEstudianteViewDTO();
        horarioViewDTO.setNombreEstudiante(estudiante.getPersona().getNombres() + " " + estudiante.getPersona().getApellidos());
        horarioViewDTO.setGradoActual(matriculaActiva.getGrado().getNombreGrado());
        horarioViewDTO.setSeccionActual(matriculaActiva.getSeccion().getNombreSeccion());
        horarioViewDTO.setPeriodoAcademicoActual(matriculaActiva.getPeriodoAcademico().getNombrePeriodo());

        List<String> diasOrdenados = Arrays.asList("Lunes", "Martes");
        List<String> horasDelDia = HorarioConstantes.HORAS_DEL_DIA;
        horarioViewDTO.setDiasOrdenados(diasOrdenados);

        Map<String, Map<String, HorarioClaseDTO>> horarioPorDiaYHora = new LinkedHashMap<>();
        for (String dia : diasOrdenados) {
            Map<String, HorarioClaseDTO> clasesPorHora = new LinkedHashMap<>();
            for (String hora : horasDelDia) {
                clasesPorHora.put(hora, null);
            }
            horarioPorDiaYHora.put(dia, clasesPorHora);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (Horario h : horariosDB) {
            String diaSemana = h.getDiaSemana();
            String horaSlotGenerado = h.getHoraInicio().format(formatter) + " - " + h.getHoraFin().format(formatter);

            // Verificamos que el slot generado esté exactamente en la lista de constantes
            Optional<String> horaSlotMatch = horasDelDia.stream()
                    .filter(hora -> hora.equals(horaSlotGenerado))
                    .findFirst();

            if (horaSlotMatch.isPresent() && horarioPorDiaYHora.containsKey(diaSemana)) {
                HorarioClaseDTO claseDTO = HorarioClaseDTO.builder()
                        .idHorario(h.getIdHorario())
                        .diaSemana(diaSemana)
                        .horaInicio(h.getHoraInicio())
                        .horaFin(h.getHoraFin())
                        .nombreCurso(h.getCurso().getNombreCurso())
                        .codigoCurso(h.getCurso().getCodigoCurso())
                        .nombreDocente(h.getDocente() != null && h.getDocente().getPersona() != null
                                ? h.getDocente().getPersona().getNombres() + " " + h.getDocente().getPersona().getApellidos()
                                : "No asignado")
                        .nombreSalon(h.getSalon().getNombreSalon())
                        .build();

                horarioPorDiaYHora.get(diaSemana).put(horaSlotMatch.get(), claseDTO);
            }
        }

        System.out.println("------ MAPA COMPLETO HORARIO ------");
        for (String dia : diasOrdenados) {
            System.out.println("Día: " + dia);
            Map<String, HorarioClaseDTO> mapaHoras = horarioPorDiaYHora.get(dia);
            for (String hora : horasDelDia) {
                HorarioClaseDTO clase = mapaHoras.get(hora);
                System.out.println("  " + hora + " => " + (clase != null ? clase.getNombreCurso() : "-"));
            }
        }

        horarioViewDTO.setHorarioPorDiaYHora(horarioPorDiaYHora);

        boolean sinHorario = horarioPorDiaYHora.values().stream()
                .flatMap(map -> map.values().stream())
                .allMatch(Objects::isNull);

        horarioViewDTO.setMensajeSinHorario(sinHorario ? "No hay horario de clases asignado para tu matrícula actual." : null);

        return horarioViewDTO;
    }
}

