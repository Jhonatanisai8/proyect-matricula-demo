package com.isai.demowebregistrationsystem.services.impl;

import com.isai.demowebregistrationsystem.exceptions.ResourceNotFoundException;
import com.isai.demowebregistrationsystem.model.dtos.apoderado.DashboardApoderadpDTO;
import com.isai.demowebregistrationsystem.model.dtos.apoderado.EstudianteListaApoderadoDTO;
import com.isai.demowebregistrationsystem.model.dtos.estudiantes.EstudianteRegistroDTO;
import com.isai.demowebregistrationsystem.model.dtos.registroInicioSesion.RegistroApoderadoDTO;
import com.isai.demowebregistrationsystem.model.entities.*;
import com.isai.demowebregistrationsystem.model.enums.Rol;
import com.isai.demowebregistrationsystem.model.dtos.ApoderadoDTO;
import com.isai.demowebregistrationsystem.repositorys.*;
import com.isai.demowebregistrationsystem.services.ApoderadoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ApoderadoServiceImpl implements ApoderadoService {

    private final ApoderadoRepository apoderadoRepository;

    private final UsuarioRepository usuarioRepository;

    private final AlmacenArchivoImpl almacenArchivo;

    private final PersonaRepository personaRepository;
    private final PasswordEncoder passwordEncoder;
    private final EstudianteRepository estudianteRepository;
    private final PeriodoAcademicoRepository periodoAcademicoRepository;
    private final MatriculaRepository matriculaRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<ApoderadoDTO> buscarApoderados(String dni, Pageable pageable) {
        Page<Apoderado> apoderadoPage;
        if (StringUtils.hasText(dni)) {
            apoderadoPage = apoderadoRepository.findByPersonaDniContainingIgnoreCase(dni, pageable);
        } else {
            apoderadoPage = apoderadoRepository.findAll(pageable);
        }
        return apoderadoPage.map(this::convertirA_DTO);
    }

    @Override
    public Optional<ApoderadoDTO> buscarApoderadoPorId(Integer idApoderado) {
        return apoderadoRepository.findById(idApoderado)
                .map(apoderado -> {
                    ApoderadoDTO dto = convertirA_DTO(apoderado);
                    // Cargar datos del usuario si existe
                    usuarioRepository.findByPersonaIdPersona(apoderado.getPersona().getIdPersona())
                            .ifPresent(usuario -> {
                                dto.setIdUsuario(usuario.getIdUsuario());
                                dto.setUserName(usuario.getUserName());
                            });
                    return dto;
                });
    }

    @Override
    public ApoderadoDTO guardarApoderado(ApoderadoDTO apoderadoDTO) {
        Persona persona;
        if (apoderadoDTO.getIdPersona() != null) { // Es una edición de Persona existente
            persona = personaRepository.findById(apoderadoDTO.getIdPersona())
                    .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con ID: " + apoderadoDTO.getIdPersona()));
            Optional<Persona> existingPersonaByDni = personaRepository.findByDni(apoderadoDTO.getDni());
            if (existingPersonaByDni.isPresent() && !existingPersonaByDni.get().getIdPersona().equals(apoderadoDTO.getIdPersona())) {
                throw new IllegalArgumentException("El DNI " + apoderadoDTO.getDni() + " ya está registrado para otra persona.");
            }
        } else {
            if (personaRepository.existsByDni(apoderadoDTO.getDni())) {
                throw new IllegalArgumentException("Ya existe una persona con el DNI: " + apoderadoDTO.getDni());
            }
            persona = new Persona();
        }

        persona.setDni(apoderadoDTO.getDni());
        persona.setNombres(apoderadoDTO.getNombres());
        persona.setApellidos(apoderadoDTO.getApellidos());
        persona.setFechaNacimiento(apoderadoDTO.getFechaNacimiento());
        persona.setGenero(apoderadoDTO.getGenero());
        persona.setDireccion(apoderadoDTO.getDireccion());
        persona.setTelefono(apoderadoDTO.getTelefono());
        persona.setEmailPersonal(apoderadoDTO.getEmailPersonal());
        persona.setEstadoCivil(apoderadoDTO.getEstadoCivil());
        persona.setTipoDocumento(apoderadoDTO.getTipoDocumento());
        String ruta = "";
        if (apoderadoDTO.getFoto() != null
                && !apoderadoDTO.getFoto().isEmpty()) {
            ruta = almacenArchivo.almacenarArchivo(apoderadoDTO.getFoto());
            persona.setFotoUrl(ruta);
        }
        Persona savedPersona = personaRepository.save(persona);
        Apoderado apoderado;
        if (apoderadoDTO.getIdApoderado() != null) {
            apoderado = apoderadoRepository.findById(apoderadoDTO.getIdApoderado())
                    .orElseThrow(() -> new EntityNotFoundException("Apoderado no encontrado con ID: " + apoderadoDTO.getIdApoderado()));
        } else {
            apoderado = new Apoderado();
            apoderado.setPersona(savedPersona);
        }

        apoderado.setOcupacion(apoderadoDTO.getOcupacion());
        apoderado.setLugarTrabajo(apoderadoDTO.getLugarTrabajo());
        apoderado.setTelefonoTrabajo(apoderadoDTO.getTelefonoTrabajo());
        apoderado.setParentesco(apoderadoDTO.getParentesco());
        apoderado.setNivelEducativo(apoderadoDTO.getNivelEducativo());
        apoderado.setIngresoMensual(apoderadoDTO.getIngresoMensual());
        apoderado.setEsPrincipal(apoderadoDTO.getEsPrincipal());
        apoderado.setAutorizadoRecoger(apoderadoDTO.getAutorizadoRecoger());
        apoderado.setReferenciaPersonal(apoderadoDTO.getReferenciaPersonal());
        apoderado.setTelefonoReferencia(apoderadoDTO.getTelefonoReferencia());

        Apoderado savedApoderado = apoderadoRepository.save(apoderado);

        Usuario usuario;
        if (apoderadoDTO.getIdUsuario() != null) { // Es una edición de Usuario existente
            usuario = usuarioRepository.findById(apoderadoDTO.getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + apoderadoDTO.getIdUsuario()));

            Optional<Usuario> existingUserByUsername = usuarioRepository.findByUserName(apoderadoDTO.getUserName());
            if (existingUserByUsername.isPresent() && !existingUserByUsername.get().getIdUsuario().equals(apoderadoDTO.getIdUsuario())) {
                throw new IllegalArgumentException("El nombre de usuario '" + apoderadoDTO.getUserName() + "' ya está en uso.");
            }
        } else {
            if (usuarioRepository.existsByUserName(apoderadoDTO.getUserName())) {
                throw new IllegalArgumentException("El nombre de usuario '" + apoderadoDTO.getUserName() + "' ya está en uso.");
            }
            usuario = new Usuario();
            usuario.setFechaCreacion(LocalDateTime.now());
            usuario.setActivo(true);
            //  usuario.setIntentosFallidos(0);
            usuario.setRol(Rol.APODERADO);
            usuario.setPersona(savedPersona);
        }

        usuario.setUserName(apoderadoDTO.getUserName());
        if (apoderadoDTO.getPassword() != null && !apoderadoDTO.getPassword().isEmpty()) {
            if (!apoderadoDTO.getPassword().equals(apoderadoDTO.getConfirmPassword())) {
                throw new IllegalArgumentException("Las contraseñas no coinciden.");
            }
            usuario.setPasswordHash(passwordEncoder.encode(apoderadoDTO.getPassword()));
        }

        usuarioRepository.save(usuario);

        return convertirA_DTO(savedApoderado);
    }

    @Override
    public void eliminarApoderado(Integer idApoderado) {
        Apoderado apoderado = apoderadoRepository.findById(idApoderado)
                .orElseThrow(() -> new EntityNotFoundException("Apoderado no encontrado con ID: " + idApoderado));
        Persona persona = apoderado.getPersona();
        usuarioRepository.findByPersonaIdPersona(persona.getIdPersona())
                .ifPresent(usuarioRepository::delete);
        apoderadoRepository.delete(apoderado);
        personaRepository.delete(persona);
    }

    private ApoderadoDTO convertirA_DTO(Apoderado apoderado) {
        ApoderadoDTO dto = new ApoderadoDTO();
        dto.setIdApoderado(apoderado.getIdApoderado());
        if (apoderado.getPersona() != null) {
            Persona persona = apoderado.getPersona();
            dto.setIdPersona(persona.getIdPersona());
            dto.setNombres(persona.getNombres());
            dto.setApellidos(persona.getApellidos());
            dto.setDni(persona.getDni());
            dto.setEmailPersonal(persona.getEmailPersonal());
            dto.setTelefono(persona.getTelefono());
            dto.setFechaNacimiento(persona.getFechaNacimiento());
            dto.setDireccion(persona.getDireccion());
            dto.setGenero(persona.getGenero());
            dto.setEstadoCivil(persona.getEstadoCivil());
            dto.setTipoDocumento(persona.getTipoDocumento());
            dto.setRutaImagen(persona.getFotoUrl());
        }
        dto.setOcupacion(apoderado.getOcupacion());
        dto.setLugarTrabajo(apoderado.getLugarTrabajo());
        dto.setTelefonoTrabajo(apoderado.getTelefonoTrabajo());
        dto.setParentesco(apoderado.getParentesco());
        dto.setNivelEducativo(apoderado.getNivelEducativo());
        dto.setIngresoMensual(apoderado.getIngresoMensual());
        dto.setEsPrincipal(apoderado.getEsPrincipal());
        dto.setAutorizadoRecoger(apoderado.getAutorizadoRecoger());
        dto.setReferenciaPersonal(apoderado.getReferenciaPersonal());
        dto.setTelefonoReferencia(apoderado.getTelefonoReferencia());

        usuarioRepository.findByPersonaIdPersona(apoderado.getPersona().getIdPersona())
                .ifPresent(usuario -> {
                    dto.setIdUsuario(usuario.getIdUsuario());
                    dto.setUserName(usuario.getUserName());
                });
        return dto;

    }

    @Override
    public Apoderado registrarNuevoApoderado(RegistroApoderadoDTO apoderadoDTO) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardApoderadpDTO obtenerDatosDashboardApoderado(String username) throws ResourceNotFoundException {
        Usuario usuario = usuarioRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));

        Apoderado apoderado = apoderadoRepository.findByPersonaIdPersona(usuario.getPersona().getIdPersona())
                .orElseThrow(() -> new ResourceNotFoundException("Apoderado no encontrado para el usuario: " + username));

        long totalHijos = estudianteRepository.countByApoderadoPrincipal_IdApoderado(apoderado.getIdApoderado());

        return DashboardApoderadpDTO.builder()
                .nombresCompletos(apoderado.getPersona().getNombres() + " " + apoderado.getPersona().getApellidos())
                .dni(apoderado.getPersona().getDni())
                .emailPersonal(apoderado.getPersona().getEmailPersonal())
                .telefono(apoderado.getPersona().getTelefono())
                .ocupacion(apoderado.getOcupacion())
                .nivelEducativo(apoderado.getNivelEducativo())
                .totalHijosVinculados((int) totalHijos)
                .build();
    }

    @Override
    public Estudiante registrarNuevoEstudiante(EstudianteRegistroDTO dto, String usernameApoderado) throws IllegalArgumentException, ResourceNotFoundException {
        //obtenemos el apoderado que esta registrando
        Usuario usuarioApoderado = usuarioRepository.findByUserName(usernameApoderado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario Apoderado no encontrado: " + usernameApoderado));
        Apoderado apoderado = apoderadoRepository.findByPersonaIdPersona(usuarioApoderado.getPersona().getIdPersona())
                .orElseThrow(() -> new ResourceNotFoundException("Apoderado no encontrado para el usuario: " + usernameApoderado));

        //verificamos que si la persona exite por dni
        Optional<Persona> personaExistente = personaRepository.findByDni(dto.getDni());
        Persona personaEstudiante;

        if (personaExistente.isPresent()) {
            personaEstudiante = personaExistente.get();
            if (estudianteRepository.findByPersonaIdPersona(personaEstudiante.getIdPersona()).isPresent()) {
                throw new IllegalArgumentException("Ya existe un estudiante registrado con el DNI: " + dto.getDni());
            }
            if (apoderado.getPersona().getIdPersona().equals(personaEstudiante.getIdPersona())) {
                throw new IllegalArgumentException("El DNI ingresado pertenece al apoderado logueado. No puede registrarse a sí mismo como estudiante.");
            }

            personaEstudiante.setNombres(dto.getNombres());
            personaEstudiante.setApellidos(dto.getApellidos());
            personaEstudiante.setFechaNacimiento(dto.getFechaNacimiento());
            personaEstudiante.setGenero(dto.getGenero());
            personaEstudiante.setDireccion(dto.getDireccion());
            personaEstudiante.setTelefono(dto.getTelefono());
            personaEstudiante.setEmailPersonal(dto.getEmailPersonal());
            personaEstudiante.setEstadoCivil(dto.getEstadoCivil());
            personaEstudiante.setTipoDocumento(dto.getTipoDocumento());
            personaEstudiante.setActivo(true);
            personaRepository.save(personaEstudiante);

        } else {
            personaEstudiante = Persona.builder()
                    .dni(dto.getDni())
                    .nombres(dto.getNombres())
                    .apellidos(dto.getApellidos())
                    .fechaNacimiento(dto.getFechaNacimiento())
                    .genero(dto.getGenero())
                    .direccion(dto.getDireccion())
                    .telefono(dto.getTelefono())
                    .emailPersonal(dto.getEmailPersonal())
                    .estadoCivil(dto.getEstadoCivil())
                    .tipoDocumento(dto.getTipoDocumento())
                    .activo(true)
                    .fechaRegistro(LocalDateTime.now())
                    .build();
            personaRepository.save(personaEstudiante);
        }
        //creamos el estudiante y vincularlo a la persona y al apoderado
        Estudiante nuevoEstudiante = new Estudiante();
        nuevoEstudiante.setPersona(personaEstudiante); // Vincular con la Persona creada/existente

        nuevoEstudiante.setCodigoEstudiante("EST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        nuevoEstudiante.setEmailEducativo(dto.getEmailEducativo());
        nuevoEstudiante.setGradoAnterior(dto.getGradoAnterior());
        nuevoEstudiante.setInstitucionProcedencia(dto.getInstitucionProcedencia());
        nuevoEstudiante.setTipoSangre(dto.getTipoSangre());
        nuevoEstudiante.setAlergias(dto.getAlergias());
        nuevoEstudiante.setContactoEmergencia(dto.getContactoEmergencia());
        nuevoEstudiante.setTelefonoEmergencia(dto.getTelefonoEmergencia());
        nuevoEstudiante.setSeguroEscolar(dto.getSeguroEscolar());
        nuevoEstudiante.setObservacionesMedicas(dto.getObservacionesMedicas());

        nuevoEstudiante.setApoderadoPrincipal(apoderado);

        return estudianteRepository.save(nuevoEstudiante);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstudianteListaApoderadoDTO> obtenerHijosDelApoderado(String usernameApoderado) throws ResourceNotFoundException {
        Usuario usuario = usuarioRepository.findByUserName(usernameApoderado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + usernameApoderado));

        Apoderado apoderado = apoderadoRepository.findByPersonaIdPersona(usuario.getPersona().getIdPersona())
                .orElseThrow(() -> new ResourceNotFoundException("Apoderado no encontrado para el usuario: " + usernameApoderado));

        List<Estudiante> estudiantes = estudianteRepository.findByApoderadoPrincipal_IdApoderado(apoderado.getIdApoderado());

        PeriodoAcademico periodoActual = periodoAcademicoRepository.findByEstado("PENDIENTE")
                .orElse(null);
        return estudiantes.stream()
                .map(estudiante -> {
                    AtomicReference<Integer> idMatriculaActualRef = new AtomicReference<>(null);

                    if (periodoActual != null) {
                        Optional<Matricula> matriculaOptional = matriculaRepository
                                .findByEstudiante_IdEstudianteAndPeriodoAcademico_IdPeriodoAndEstadoMatricula(
                                        estudiante.getIdEstudiante(),
                                        periodoActual.getIdPeriodo(),
                                        "ACTIVA"
                                );
                        matriculaOptional.ifPresent(matricula -> idMatriculaActualRef.set(matricula.getIdMatricula()));
                    }

                    return EstudianteListaApoderadoDTO.builder()
                            .idEstudiante(estudiante.getIdEstudiante())
                            .idMatriculaActual(idMatriculaActualRef.get())
                            .codigoEstudiante(estudiante.getCodigoEstudiante())
                            .nombresCompletos(estudiante.getPersona().getNombres() + " " + estudiante.getPersona().getApellidos())
                            .dni(estudiante.getPersona().getDni())
                            .fechaNacimiento(estudiante.getPersona().getFechaNacimiento())
                            .emailEducativo(estudiante.getEmailEducativo())
                            .telefonoEmergencia(estudiante.getTelefonoEmergencia())
                            .seguroEscolar(estudiante.getSeguroEscolar())
                            .parentescoConApoderadoPrincipal(apoderado.getParentesco())
                            .build();
                })
                .collect(Collectors.toList());
    }
}