package com.isai.demowebregistrationsystem.services.impl;

import com.isai.demowebregistrationsystem.model.entities.Usuario;
import com.isai.demowebregistrationsystem.repositorys.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl
        implements UserDetailsService {


    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));

        System.out.println(usuario.getUserName());
        System.out.println(usuario.getPasswordHash());
        System.out.println(usuario.getRol().name());
        String roleName = "ROLE_" + usuario.getRol();

        return new User(
                usuario.getUserName(),
                usuario.getPasswordHash(),
                usuario.getActivo(),
                true,
                true,
                true,
                Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(roleName))
        );
    }
}
