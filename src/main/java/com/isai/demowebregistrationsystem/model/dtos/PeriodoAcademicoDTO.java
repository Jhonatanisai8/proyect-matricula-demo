package com.isai.demowebregistrationsystem.model.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PeriodoAcademicoDTO {

    private Integer idPeriodo;

    @NotBlank(message = "El nombre del período es obligatorio.")
    @Size(max = 100, message = "El nombre del período no puede exceder los 100 caracteres.")
    private String nombrePeriodo;

    @NotNull(message = "El año académico es obligatorio.")
    @Min(value = 2000, message = "El año académico debe ser al menos 2000.")
    private Integer anoAcademico;

    @NotNull(message = "La fecha de inicio es obligatoria.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;

    @Size(max = 50, message = "El estado no puede exceder los 50 caracteres.")
    private String estado;

    private Boolean activo;
}
