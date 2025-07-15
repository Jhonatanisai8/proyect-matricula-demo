package com.isai.demowebregistrationsystem.model.dtos.docente;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocenteRegistroDTO {
    private Integer idDocente;

    private Integer idPersona;

    @NotBlank(message = "El DNI no puede estar vacío.")
    @Size(min = 8, max = 20, message = "El DNI debe tener entre 8 y 20 caracteres.")
    private String dni;

    @NotBlank(message = "Los nombres no pueden estar vacíos.")
    @Size(max = 100, message = "Los nombres no pueden exceder los 100 caracteres.")
    private String nombres;

    @NotBlank(message = "Los apellidos no pueden estar vacíos.")
    @Size(max = 100, message = "Los apellidos no pueden exceder los 100 caracteres.")
    private String apellidos;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Past(message = "La fecha de nacimiento debe ser en el pasado.")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El género no puede estar vacío.")
    @Size(max = 10, message = "El género no puede exceder los 10 caracteres.")
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

    @NotBlank(message = "El tipo de documento no puede estar vacío.")
    @Size(max = 50, message = "El tipo de documento no puede exceder los 50 caracteres.")
    private String tipoDocumento;

    private String fotoUrlPersona;


    private String codigoDocente;

    @Email(message = "Debe ser un formato de correo electrónico institucional válido.")
    @Size(max = 100, message = "El email institucional no puede exceder los 100 caracteres.")
    private String emailInstitucional;

    @NotBlank(message = "La especialidad principal no puede estar vacía.")
    @Size(max = 100, message = "La especialidad principal no puede exceder los 100 caracteres.")
    private String especialidadPrincipal;

    @Size(max = 100, message = "La especialidad secundaria no puede exceder los 100 caracteres.")
    private String especialidadSecundaria;

    @NotBlank(message = "El título profesional no puede estar vacío.")
    @Size(max = 100, message = "El título profesional no puede exceder los 100 caracteres.")
    private String tituloProfesional;

    @Size(max = 255, message = "La universidad de egreso no puede exceder los 255 caracteres.")
    private String universidadEgreso;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de contratación es obligatoria.")
    @PastOrPresent(message = "La fecha de contratación no puede ser en el futuro.")
    private LocalDate fechaContratacion;

    @NotNull(message = "El salario base no puede estar vacío.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El salario base debe ser mayor a 0.")
    @Digits(integer = 8, fraction = 2, message = "El salario base debe tener hasta 8 dígitos enteros y 2 decimales.")
    private BigDecimal salarioBase;

    @NotBlank(message = "El tipo de contrato no puede estar vacío.")
    @Size(max = 50, message = "El tipo de contrato no puede exceder los 50 caracteres.")
    private String tipoContrato;

    @NotBlank(message = "El estado laboral no puede estar vacío.")
    @Size(max = 50, message = "El estado laboral no puede exceder los 50 caracteres.")
    private String estadoLaboral;

    @Min(value = 0, message = "Los años de experiencia no pueden ser negativos.")
    @NotNull(message = "Los años de experiencia son obligatorios.")
    private Integer anosExperiencia;

    @Size(max = 255, message = "La URL del CV no puede exceder los 255 caracteres.")
    private String cvUrl;

    @Size(max = 255, message = "La URL de la foto no puede exceder los 255 caracteres.")
    private String fotoUrlDocente;

    private MultipartFile foto;

    @NotNull(message = "Debe indicar si el docente es coordinador.")
    private Boolean coordinador;

    private String username;
    private String password;
    private String confirmPassword;
}
