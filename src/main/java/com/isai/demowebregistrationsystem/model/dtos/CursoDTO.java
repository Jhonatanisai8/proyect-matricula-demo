package com.isai.demowebregistrationsystem.model.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CursoDTO {

    private Integer idCurso;

    @NotBlank(message = "El nombre del curso es obligatorio.")
    @Size(max = 100, message = "El nombre del curso no puede exceder los 100 caracteres.")
    private String nombreCurso;

    @NotBlank(message = "El código del curso es obligatorio.")
    @Size(max = 20, message = "El código del curso no puede exceder los 20 caracteres.")
    private String codigoCurso;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres.")
    private String descripcion;

    @Size(max = 100, message = "El área de conocimiento no puede exceder los 100 caracteres.")
    private String areaConocimiento;

    @Min(value = 0, message = "Los créditos no pueden ser negativos.")
    private Integer creditos;

    @Min(value = 0, message = "Las horas semanales no pueden ser negativas.")
    private Integer horasSemanales;

    @NotNull(message = "Debe especificar si el curso es obligatorio.")
    private Boolean esObligatorio;

    @Size(max = 255, message = "Los prerequisitos no pueden exceder los 255 caracteres.")
    private String prerequisitos;

    private String competencias;

    private Boolean activo;

    @NotNull(message = "Debe asociar el curso a un grado.")
    private Integer idGrado;
    private String nombreGrado;
}
