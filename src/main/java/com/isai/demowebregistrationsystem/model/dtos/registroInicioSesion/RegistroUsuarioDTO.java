package com.isai.demowebregistrationsystem.model.dtos.registroInicioSesion;

import com.isai.demowebregistrationsystem.model.enums.Rol;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class RegistroUsuarioDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(min = 4, max = 50, message = "El nombre de usuario debe tener entre 4 y 50 caracteres.")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    private String password;

    @NotBlank(message = "Confirmar contraseña es obligatorio.")
    private String confirmPassword;

    @NotNull(message = "El rol es obligatorio.")
    private Rol rol;

    @NotBlank(message = "Los nombres son obligatorios.")
    @Size(max = 100, message = "Los nombres no pueden exceder los 100 caracteres.")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios.")
    @Size(max = 100, message = "Los apellidos no pueden exceder los 100 caracteres.")
    private String apellidos;

    @NotBlank(message = "El DNI es obligatorio.")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe contener exactamente 8 dígitos.")
    private String dni;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Past(message = "La fecha de nacimiento debe ser en el pasado.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El género es obligatorio.")
    @Size(max = 10, message = "El género no puede exceder los 10 caracteres.")
    private String genero;

    @Size(max = 255, message = "La dirección no puede exceder los 255 caracteres.")
    private String direccion;

    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres.")
    private String telefono;

    @Email(message = "Formato de email incorrecto.")
    @Size(max = 100, message = "El email personal no puede exceder los 100 caracteres.")
    private String emailPersonal;

    @Size(max = 50, message = "El estado civil no puede exceder los 50 caracteres.")
    private String estadoCivil;

    @NotBlank(message = "El tipo de documento es obligatorio.")
    @Size(max = 50, message = "El tipo de documento no puede exceder los 50 caracteres.")
    private String tipoDocumento;

}