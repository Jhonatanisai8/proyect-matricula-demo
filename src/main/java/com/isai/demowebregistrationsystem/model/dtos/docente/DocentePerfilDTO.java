package com.isai.demowebregistrationsystem.model.dtos.docente;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class DocentePerfilDTO {

    private Integer idDocente;
    private Integer idPersona;


    @NotBlank(message = "Los nombres no pueden estar vacíos.")
    @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres.")
    private String nombres;

    @NotBlank(message = "Los apellidos no pueden estar vacíos.")
    @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres.")
    private String apellidos;

    @NotBlank(message = "El DNI no puede estar vacío.")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 caracteres.")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe contener solo dígitos.")
    private String dni;

    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres.")
    private String direccion;

    @Email(message = "Debe ser un formato de correo electrónico válido.")
    @Size(max = 100, message = "El email personal no puede exceder 100 caracteres.")
    private String emailPersonal;

    @Size(max = 50, message = "El estado civil no puede exceder 50 caracteres.")
    private String estadoCivil;

    @PastOrPresent(message = "La fecha de nacimiento no puede ser en el futuro.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @Size(max = 255, message = "La URL de la foto no puede exceder 255 caracteres.")
    private String fotoUrl;

    private MultipartFile foto;

    @Size(max = 10, message = "El género no puede exceder 10 caracteres.")
    private String genero;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres.")
    private String telefono;

    @Size(max = 50, message = "El tipo de documento no puede exceder 50 caracteres.")
    private String tipoDocumento;


    @NotNull(message = "Los años de experiencia son obligatorios.")
    private Integer anosExperiencia;

    @Size(max = 255, message = "La URL del CV no puede exceder 255 caracteres.")
    private String cvUrl;

    @Email(message = "Debe ser un formato de correo electrónico institucional válido.")
    @Size(max = 100, message = "El email institucional no puede exceder 100 caracteres.")
    private String emailInstitucional;

    @Size(max = 100, message = "La especialidad principal no puede exceder 100 caracteres.")
    private String especialidadPrincipal;

    @Size(max = 100, message = "La especialidad secundaria no puede exceder 100 caracteres.")
    private String especialidadSecundaria;

    @Size(max = 100, message = "El título profesional no puede exceder 100 caracteres.")
    private String tituloProfesional;

    @Size(max = 255, message = "La universidad de egreso no puede exceder 255 caracteres.")
    private String universidadEgreso;


    private String codigoDocente;
    private Boolean coordinador;
    private String estadoLaboral;
    private LocalDate fechaContratacion;
    private BigDecimal salarioBase;
    private String tipoContrato;
    private Boolean personaActivo;

    public DocentePerfilDTO() {
        this.personaActivo = true;
    }
}
