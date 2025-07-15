package com.isai.demowebregistrationsystem.model.dtos;

import com.isai.demowebregistrationsystem.model.enums.Rol;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Integer idUsuario;

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(min = 4, max = 50, message = "El nombre de usuario debe tener entre 4 y 50 caracteres.")
    private String userName;

    @Size(min = 6, max = 255, message = "La contraseña debe tener al menos 6 caracteres.")
    private String password;
    private String confirmPassword;

    @NotNull(message = "El rol es obligatorio.")
    private Rol rol;

    //private LocalDateTime ultimoAcceso;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    //private Integer intentosFallidos;

    @NotNull(message = "Debe asociar el usuario a una persona.")
    private Integer personaId;
    private String nombreCompletoPersona;

}