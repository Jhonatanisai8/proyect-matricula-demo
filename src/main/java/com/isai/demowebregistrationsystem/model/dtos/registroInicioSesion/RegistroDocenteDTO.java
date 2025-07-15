package com.isai.demowebregistrationsystem.model.dtos.registroInicioSesion;

import com.isai.demowebregistrationsystem.model.enums.Rol;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class RegistroDocenteDTO
        extends RegistroUsuarioDTO {

    @NotBlank(message = "El código de docente es obligatorio.")
    @Size(max = 50, message = "El código de docente no puede exceder los 50 caracteres.")
    private String codigoDocente;

    private Boolean coordinador;

    @Size(max = 255, message = "La URL del CV no puede exceder los 255 caracteres.")
    private String cvUrl;

    @NotBlank(message = "El email institucional es obligatorio.")
    @Size(max = 100, message = "El email institucional no puede exceder los 100 caracteres.")
    @Email(message = "Formato de email institucional incorrecto.")
    private String emailInstitucional;

    @NotBlank(message = "La especialidad principal es obligatoria.")
    @Size(max = 100, message = "La especialidad principal no puede exceder los 100 caracteres.")
    private String especialidadPrincipal;

    @Size(max = 100, message = "La especialidad secundaria no puede exceder los 100 caracteres.")
    private String especialidadSecundaria;

    @Size(max = 50, message = "El estado laboral no puede exceder los 50 caracteres.")
    private String estadoLaboral;

    private LocalDate fechaContratacion;

    private BigDecimal salarioBase;

    @Size(max = 50, message = "El tipo de contrato no puede exceder los 50 caracteres.")
    private String tipoContrato;

    @Size(max = 100, message = "El título profesional no puede exceder los 100 caracteres.")
    private String tituloProfesional;

    @Size(max = 255, message = "La universidad de egreso no puede exceder los 255 caracteres.")
    private String universidadEgreso;

    @Min(value = 0, message = "Los años de experiencia no pueden ser negativos.")
    private Integer anosExperiencia;

    public RegistroDocenteDTO() {
        this.setRol(Rol.DOCENTE);
        this.coordinador = false;
    }
}
