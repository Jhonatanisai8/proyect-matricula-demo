package com.isai.demowebregistrationsystem.model.dtos.apoderado;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * DTO para el registro de un nuevo estudiante por parte del apoderado.
 * Contiene campos combinados de Persona y Estudiante.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstudianteRegistroApoderadoDTO {

    // --- Campos de Persona ---
    @NotBlank(message = "El DNI es obligatorio.")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos.")
    @Pattern(regexp = "\\d+", message = "El DNI debe contener solo números.")
    private String dni;

    @NotBlank(message = "Los nombres son obligatorios.")
    @Size(max = 100, message = "Los nombres no pueden exceder los 100 caracteres.")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios.")
    @Size(max = 100, message = "Los apellidos no pueden exceder los 100 caracteres.")
    private String apellidos;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Past(message = "La fecha de nacimiento debe ser en el pasado.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El género es obligatorio.")
    private String genero;

    @Size(max = 255, message = "La dirección no puede exceder los 255 caracteres.")
    private String direccion;

    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres.")
    private String telefono;

    @Email(message = "Debe ser un formato de correo electrónico válido.")
    @Size(max = 100, message = "El email personal no puede exceder los 100 caracteres.")
    private String emailPersonal;

    @Size(max = 50, message = "El estado civil no puede exceder los 50 caracteres.")
    private String estadoCivil;

    @NotBlank(message = "El tipo de documento es obligatorio.")
    @Size(max = 50, message = "El tipo de documento no puede exceder los 50 caracteres.")
    private String tipoDocumento;


    // --- Campos de Estudiante ---

    @Email(message = "Debe ser un formato de correo electrónico válido.")
    @Size(max = 100, message = "El email educativo no puede exceder los 100 caracteres.")
    private String emailEducativo;

    @Size(max = 100, message = "El grado anterior no puede exceder los 100 caracteres.")
    private String gradoAnterior;

    @Size(max = 255, message = "La institución de procedencia no puede exceder los 255 caracteres.")
    private String institucionProcedencia;

    @Size(max = 10, message = "El tipo de sangre no puede exceder los 10 caracteres.")
    private String tipoSangre;

    @Size(max = 255, message = "Las alergias no pueden exceder los 255 caracteres.")
    private String alergias;

    @Size(max = 100, message = "El contacto de emergencia no puede exceder los 100 caracteres.")
    private String contactoEmergencia;

    @Size(max = 20, message = "El teléfono de emergencia no puede exceder los 20 caracteres.")
    private String telefonoEmergencia;

    @NotNull(message = "Debe especificar si tiene seguro escolar.")
    private Boolean seguroEscolar;

    @Size(max = 1000, message = "Las observaciones médicas no pueden exceder los 1000 caracteres.")
    private String observacionesMedicas;


}
