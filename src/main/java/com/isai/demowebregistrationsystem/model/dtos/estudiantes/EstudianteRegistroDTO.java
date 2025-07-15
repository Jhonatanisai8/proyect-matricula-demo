package com.isai.demowebregistrationsystem.model.dtos.estudiantes;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class EstudianteRegistroDTO {
    // IDs para editar (serán null en creación)
    private Integer idEstudiante;
    private Integer idPersona;

    // --- Campos de Persona ---
    @NotBlank(message = "Nombres son obligatorios")
    @Size(max = 100, message = "Nombres no pueden exceder 100 caracteres")
    private String nombres;

    @NotBlank(message = "Apellidos son obligatorios")
    @Size(max = 100, message = "Apellidos no pueden exceder 100 caracteres")
    private String apellidos;

    @NotBlank(message = "DNI es obligatorio")
    @Size(min = 8, max = 8, message = "DNI debe tener 8 dígitos")
    @Pattern(regexp = "^\\d{8}$", message = "DNI debe contener solo dígitos")
    private String dni;

    @Size(max = 255, message = "Dirección no puede exceder 255 caracteres")
    private String direccion;

    @Email(message = "Email personal inválido")
    @Size(max = 100, message = "Email personal no puede exceder 100 caracteres")
    private String emailPersonal;

    @Size(max = 50, message = "Estado civil no puede exceder 50 caracteres")
    private String estadoCivil;

    @NotNull(message = "Fecha de nacimiento es obligatoria")
    @PastOrPresent(message = "Fecha de nacimiento no puede ser en el futuro")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @Size(max = 255, message = "URL de foto no puede exceder 255 caracteres")
    private String fotoUrl;

    private MultipartFile foto;

    @Size(max = 10, message = "Género no puede exceder 10 caracteres")
    private String genero;

    @Size(max = 20, message = "Teléfono no puede exceder 20 caracteres")
    private String telefono;

    @Size(max = 50, message = "Tipo de documento no puede exceder 50 caracteres")
    private String tipoDocumento;

    @NotNull(message = "El estado 'activo' de la persona es obligatorio")
    private Boolean personaActivo;

    // --- Campos de Estudiante ---
    private String codigoEstudiante;

    @Email(message = "Email educativo inválido")
    @Size(max = 100, message = "Email educativo no puede exceder 100 caracteres")
    private String emailEducativo;

    @Size(max = 255, message = "Institución de procedencia no puede exceder 255 caracteres")
    private String institucionProcedencia;

    @Size(max = 100, message = "Grado anterior no puede exceder 100 caracteres")
    private String gradoAnterior;

    @NotNull(message = "Seguro escolar es obligatorio")
    private Boolean seguroEscolar;

    @Size(max = 255, message = "Alergias no pueden exceder 255 caracteres")
    private String alergias;

    @Size(max = 1000, message = "Observaciones médicas no pueden exceder 1000 caracteres") // TEXT en DB
    private String observacionesMedicas;

    @Size(max = 100, message = "Contacto de emergencia no puede exceder 100 caracteres")
    private String contactoEmergencia;

    @Size(max = 20, message = "Teléfono de emergencia no puede exceder 20 caracteres")
    private String telefonoEmergencia;

    @Size(max = 10, message = "Tipo de sangre no puede exceder 10 caracteres")
    private String tipoSangre;

    // --- Campo para el apoderado asociado (relación) ---
    private Integer idApoderado;


    public EstudianteRegistroDTO() {
        this.personaActivo = true;
        this.seguroEscolar = false;
    }

    private Integer idUsuario;

    private String userName;

    private String password;
}
