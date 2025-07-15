package com.isai.demowebregistrationsystem.model.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteDTO {

    // --- Datos de la Persona del Estudiante ---
    @NotBlank(message = "El DNI del estudiante no puede estar vacío.")
    @Size(min = 8, max = 20, message = "El DNI del estudiante debe tener entre 8 y 20 caracteres.")
    private String dniEstudiante;

    @NotBlank(message = "Los nombres del estudiante no pueden estar vacíos.")
    @Size(max = 100, message = "Los nombres del estudiante no pueden exceder los 100 caracteres.")
    private String nombresEstudiante;

    @NotBlank(message = "Los apellidos del estudiante no pueden estar vacíos.")
    @Size(max = 100, message = "Los apellidos del estudiante no pueden exceder los 100 caracteres.")
    private String apellidosEstudiante;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de nacimiento del estudiante es obligatoria.")
    @Past(message = "La fecha de nacimiento debe ser en el pasado.")
    private LocalDate fechaNacimientoEstudiante;

    @NotBlank(message = "El género del estudiante no puede estar vacío.")
    @Size(max = 10, message = "El género no puede exceder los 10 caracteres.")
    private String generoEstudiante;

    @Size(max = 255, message = "La dirección del estudiante no puede exceder los 255 caracteres.")
    private String direccionEstudiante;

    @Size(max = 20, message = "El teléfono del estudiante no puede exceder los 20 caracteres.")
    private String telefonoEstudiante;

    @Email(message = "Debe ser un formato de correo electrónico válido.")
    @Size(max = 100, message = "El email personal del estudiante no puede exceder los 100 caracteres.")
    private String emailPersonalEstudiante;

    @Size(max = 50, message = "El estado civil del estudiante no puede exceder los 50 caracteres.")
    private String estadoCivilEstudiante;

    @NotBlank(message = "El tipo de documento del estudiante no puede estar vacío.")
    @Size(max = 50, message = "El tipo de documento no puede exceder los 50 caracteres.")
    private String tipoDocumentoEstudiante;

    // --- Datos Específicos del Estudiante ---
    @NotBlank(message = "El código de estudiante no puede estar vacío.")
    @Size(min = 5, max = 50, message = "El código de estudiante debe tener entre 5 y 50 caracteres.")
    private String codigoEstudiante;

    @Email(message = "Debe ser un formato de correo electrónico educativo válido.")
    @Size(max = 100, message = "El email educativo no puede exceder los 100 caracteres.")
    private String emailEducativoEstudiante;

    @Size(max = 100, message = "El grado anterior no puede exceder los 100 caracteres.")
    private String gradoAnteriorEstudiante;

    @Size(max = 255, message = "La institución de procedencia no puede exceder los 255 caracteres.")
    private String institucionProcedenciaEstudiante;

    @Size(max = 10, message = "El tipo de sangre no puede exceder los 10 caracteres.")
    private String tipoSangreEstudiante;

    @Size(max = 1000, message = "Las observaciones médicas no pueden exceder los 1000 caracteres.")
    private String observacionesMedicasEstudiante;

    @Size(max = 255, message = "Las alergias no pueden exceder los 255 caracteres.")
    private String alergiasEstudiante;

    @Size(max = 100, message = "El contacto de emergencia no puede exceder los 100 caracteres.")
    private String contactoEmergenciaEstudiante;

    @Size(max = 20, message = "El teléfono de emergencia no puede exceder los 20 caracteres.")
    private String telefonoEmergenciaEstudiante;

    @NotNull(message = "Debe indicar si el estudiante tiene seguro escolar.")
    private Boolean seguroEscolarEstudiante;

    @Size(max = 255, message = "La URL de la foto del estudiante no puede exceder los 255 caracteres.")
    private String fotoUrlEstudiante;

    @NotBlank(message = "El DNI del apoderado principal no puede estar vacío.") // Si se ingresa por DNI
    @Size(min = 8, max = 20, message = "El DNI del apoderado principal debe tener entre 8 y 20 caracteres.")
    private String dniApoderadoPrincipal;


    // --- Campos de Usuario ---
    @Size(min = 5, max = 50, message = "El nombre de usuario debe tener entre 5 y 50 caracteres.")
    private String username;

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    private String password;

    private String rol; //por defecto estudiante
}