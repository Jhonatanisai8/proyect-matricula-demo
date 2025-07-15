package com.isai.demowebregistrationsystem.services;


import com.isai.demowebregistrationsystem.model.dtos.registroInicioSesion.RegistroUsuarioDTO;
import com.isai.demowebregistrationsystem.model.dtos.UsuarioDTO;
import com.isai.demowebregistrationsystem.model.entities.Persona;
import com.isai.demowebregistrationsystem.model.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Page<UsuarioDTO> obtenerUsuarios(Pageable pageable);

    Optional<UsuarioDTO> obtenerUsuarioPorId(Integer id);

    UsuarioDTO guardarUsuario(UsuarioDTO usuarioDTO);

    void eliminarUsuario(Integer id);

    void alternarEstadoUsuario(Integer id);

    List<Persona> encontrarPersonasDisponibles();

    List<Persona> encontrarPersonasDisponiblesParaUsuario(Integer id);

    UsuarioDTO registrarNuevoUsuario(RegistroUsuarioDTO registroDTO);

    Optional<Usuario> findByUsername(String username);
}