package com.isai.demowebregistrationsystem.services.impl;


import com.isai.demowebregistrationsystem.model.dtos.*;
import com.isai.demowebregistrationsystem.model.dtos.registroInicioSesion.RegistroApoderadoDTO;
import com.isai.demowebregistrationsystem.model.dtos.registroInicioSesion.RegistroDocenteDTO;
import com.isai.demowebregistrationsystem.model.dtos.registroInicioSesion.RegistroEstudianteDTO;
import com.isai.demowebregistrationsystem.model.dtos.registroInicioSesion.RegistroUsuarioDTO;
import com.isai.demowebregistrationsystem.model.entities.*;
import com.isai.demowebregistrationsystem.repositorys.*;
import com.isai.demowebregistrationsystem.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {


    private final PersonaRepository personaRepository;

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;
    private final DocenteRepository docenteRepository;
    private final EstudianteRepository estudianteRepository;
    private final ApoderadoRepository apoderadoRepository;

    private Usuario convertToEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDTO, usuario, "password", "confirmPassword", "nombreCompletoPersona", "fechaCreacion", "ultimoAcceso", "intentosFallidos");
        if (usuarioDTO.getPersonaId() != null) {
            Persona persona = personaRepository.findById(usuarioDTO.getPersonaId())
                    .orElseThrow(() -> new IllegalArgumentException("Persona con ID " + usuarioDTO.getPersonaId() + " no encontrada."));
            usuario.setPersona(persona);
        }
        return usuario;
    }

    private UsuarioDTO convertToDto(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        BeanUtils.copyProperties(usuario, usuarioDTO, "passwordHash");

        if (usuario.getPersona() != null) {
            usuarioDTO.setPersonaId(usuario.getPersona().getIdPersona());
            usuarioDTO.setNombreCompletoPersona(usuario.getPersona().getNombres() + " " + usuario.getPersona().getApellidos());
        } else {
            usuarioDTO.setNombreCompletoPersona("Persona No Asignada");
        }
        return usuarioDTO;
    }


    @Transactional(readOnly = true)
    @Override
    public Page<UsuarioDTO> obtenerUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UsuarioDTO> obtenerUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public UsuarioDTO guardarUsuario(UsuarioDTO usuarioDTO) {

        if (usuarioDTO.getIdUsuario() == null || usuarioDTO.getIdUsuario() == 0) {
            if (usuarioDTO.getPassword() == null || usuarioDTO.getPassword().isEmpty()) {
                throw new IllegalArgumentException("La contraseña es obligatoria para un nuevo usuario.");
            }
            if (!usuarioDTO.getPassword().equals(usuarioDTO.getConfirmPassword())) {
                throw new IllegalArgumentException("La contraseña y su confirmación no coinciden.");
            }
        } else {
            if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
                if (!usuarioDTO.getPassword().equals(usuarioDTO.getConfirmPassword())) {
                    throw new IllegalArgumentException("La nueva contraseña y su confirmación no coinciden.");
                }
            }
        }

        Optional<Usuario> existingUserByUsername = usuarioRepository.findByUserName(usuarioDTO.getUserName());
        if (existingUserByUsername.isPresent() &&
                (usuarioDTO.getIdUsuario() == null || !existingUserByUsername.get().getIdUsuario().equals(usuarioDTO.getIdUsuario()))) {
            throw new IllegalArgumentException("Ya existe un usuario con el nombre de usuario '" + usuarioDTO.getUserName() + "'.");
        }


        Optional<Usuario> existingUserByPersona = usuarioRepository.findByPersonaIdPersona(usuarioDTO.getPersonaId());
        if (existingUserByPersona.isPresent() &&
                (usuarioDTO.getIdUsuario() == null || !existingUserByPersona.get().getIdUsuario().equals(usuarioDTO.getIdUsuario()))) {

            throw new IllegalArgumentException("La persona seleccionada ya está asociada a otro usuario.");
        }


        Usuario usuario;
        if (usuarioDTO.getIdUsuario() == null || usuarioDTO.getIdUsuario() == 0) {
            usuario = new Usuario();
            usuario.setFechaCreacion(LocalDateTime.now());
            usuario.setActivo(usuarioDTO.getActivo() != null ? usuarioDTO.getActivo() : true);
            // usuario.setIntentosFallidos(0);
        } else {
            usuario = usuarioRepository.findById(usuarioDTO.getIdUsuario())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario con ID " + usuarioDTO.getIdUsuario() + " no encontrado."));
        }


        usuario.setUserName(usuarioDTO.getUserName());
        usuario.setRol(usuarioDTO.getRol());
        usuario.setActivo(usuarioDTO.getActivo() != null ? usuarioDTO.getActivo() : false);


        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            usuario.setPasswordHash(passwordEncoder.encode(usuarioDTO.getPassword()));
        }


        Persona persona = personaRepository.findById(usuarioDTO.getPersonaId())
                .orElseThrow(() -> new IllegalArgumentException("Persona con ID " + usuarioDTO.getPersonaId() + " no encontrada."));
        usuario.setPersona(persona);

        return convertToDto(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminarUsuario(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("El usuario con ID " + id + " no existe.");
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public void alternarEstadoUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
        usuario.setActivo(!usuario.getActivo());
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Persona> encontrarPersonasDisponibles() {
        List<Persona> allPersonas = personaRepository.findAll();
        Set<Integer> personasConUsuario = usuarioRepository.findAll().stream()
                .filter(u -> u.getPersona() != null)
                .map(u -> u.getPersona().getIdPersona())
                .collect(Collectors.toSet());

        return allPersonas.stream()
                .filter(p -> !personasConUsuario.contains(p.getIdPersona()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Persona> encontrarPersonasDisponiblesParaUsuario(Integer currentUserId) {
        List<Persona> availablePersonas = encontrarPersonasDisponibles();
        if (currentUserId != null) {
            Optional<Usuario> currentUserOpt = usuarioRepository.findById(currentUserId);
            currentUserOpt.ifPresent(u -> {
                if (u.getPersona() != null) {

                    if (!availablePersonas.contains(u.getPersona())) {
                        availablePersonas.add(u.getPersona());
                    }
                }
            });
        }
        availablePersonas.sort((p1, p2) -> {
            String fullName1 = p1.getNombres() + " " + p1.getApellidos();
            String fullName2 = p2.getNombres() + " " + p2.getApellidos();
            return fullName1.compareToIgnoreCase(fullName2);
        });
        return availablePersonas;
    }

    @Override
    @Transactional
    public UsuarioDTO registrarNuevoUsuario(RegistroUsuarioDTO registroDTO) {
        if (usuarioRepository.existsByUserName(registroDTO.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario '" + registroDTO.getUsername() + "' ya está en uso.");
        }

        if (personaRepository.existsByDni(registroDTO.getDni())) {
            throw new IllegalArgumentException("Ya existe una persona registrada con el DNI '" + registroDTO.getDni() + "'.");
        }

        //creamos y guardamos la entidad Persona
        Persona persona = new Persona();
        BeanUtils.copyProperties(registroDTO, persona);
        persona.setActivo(true);
        persona.setFechaRegistro(LocalDateTime.now());
        persona = personaRepository.save(persona);

        // creamos y guardamos la entidad Usuario
        Usuario usuario = new Usuario();
        usuario.setUserName(registroDTO.getUsername());
        usuario.setPasswordHash(passwordEncoder.encode(registroDTO.getPassword()));
        usuario.setRol(registroDTO.getRol());
        usuario.setActivo(true);
        usuario.setFechaCreacion(LocalDateTime.now());
        //usuario.setIntentosFallidos(0);
        usuario.setPersona(persona);
        usuario = usuarioRepository.save(usuario);


        //  creamos y guardamos la entidad específica del rol
        switch (registroDTO.getRol()) {
            case DOCENTE:
                RegistroDocenteDTO docenteDTO = (RegistroDocenteDTO) registroDTO;
                Docente docente = new Docente();
                BeanUtils.copyProperties(docenteDTO, docente);
                docente.setPersona(persona);
                docente.setActivo(true);
                docenteRepository.save(docente);
                break;
            case ESTUDIANTE:
                RegistroEstudianteDTO estudianteDTO = (RegistroEstudianteDTO) registroDTO;
                Estudiante estudiante = new Estudiante();
                BeanUtils.copyProperties(estudianteDTO, estudiante);
                estudiante.setPersona(persona); // Asocia la Persona
                estudiante.setSeguroEscolar(estudianteDTO.getSeguroEscolar() != null ? estudianteDTO.getSeguroEscolar() : false);
                //lógica para asociar un apoderado existente
                if (estudianteDTO.getDniApoderadoPrin() != null) {
                    Apoderado apoderado = apoderadoRepository.findByPersonaDni(estudianteDTO.getDniApoderadoPrin())
                            .orElseThrow(() -> new IllegalArgumentException("Apoderado no encontrado."));
                    System.out.println(apoderado.getPersona().getNombres());
                    estudiante.setApoderadoPrincipal(apoderado);
                }
                estudianteRepository.save(estudiante);
                break;
            case APODERADO:
                RegistroApoderadoDTO apoderadoDTO = (RegistroApoderadoDTO) registroDTO;
                Apoderado apoderado = new Apoderado();
                BeanUtils.copyProperties(apoderadoDTO, apoderado);
                apoderado.setPersona(persona); // Asocia la Persona
                apoderado.setAutorizadoRecoger(apoderadoDTO.getAutorizadoRecoger() != null ? apoderadoDTO.getAutorizadoRecoger() : false);
                apoderado.setEsPrincipal(apoderadoDTO.getEsPrincipal() != null ? apoderadoDTO.getEsPrincipal() : false);
                apoderadoRepository.save(apoderado);
                break;
            case ADMIN:
                break;
            default:
                throw new IllegalArgumentException("Rol de usuario no soportado para registro.");
        }

        return new UsuarioDTO(usuario.getIdUsuario(),
                usuario.getUserName(),
                usuario.getPasswordHash(),
                usuario.getPasswordHash(),
                usuario.getRol(),
                //usuario.getUltimoAcceso(),
                usuario.getActivo(),
                usuario.getFechaCreacion(),
                //usuario.getIntentosFallidos(),
                usuario.getPersona().getIdPersona(),
                usuario.getPersona().getNombres());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUserName(username);
    }

}