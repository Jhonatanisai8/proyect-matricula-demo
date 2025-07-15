package com.isai.demowebregistrationsystem.model.dtos.secciones;

import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SeccionRegistroDTO {

    private Integer idSeccion;

    @NotBlank(message = "El nombre de la sección es obligatorio.")
    @Size(max = 10, message = "El nombre de la sección no puede exceder los 10 caracteres.")
    private String nombreSeccion;

    @NotNull(message = "Debe seleccionar un docente tutor.")
    private Integer idDocenteTutor;

    @NotNull(message = "Debe seleccionar un grado.")
    private Integer idGrado;

    @NotNull(message = "Debe seleccionar un período académico.")
    private Integer idPeriodoAcademico;

}
