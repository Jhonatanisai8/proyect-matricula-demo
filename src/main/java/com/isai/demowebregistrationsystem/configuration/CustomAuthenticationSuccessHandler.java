package com.isai.demowebregistrationsystem.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String redirectUrl = "/";

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            redirectUrl = "/admin/dashboard";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCENTE"))) {
            redirectUrl = "/docente/dashboard";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ESTUDIANTE"))) {
            redirectUrl = "/estudiante/dashboard";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_APODERADO"))) {
            redirectUrl = "/apoderado/dashboard";
        }

        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
