package com.isai.demowebregistrationsystem.model.dtos.docente;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AsignacionDocenteDTO {
    private Integer idAsignacion;

    @NotNull(message = "El docente es obligatorio.")
    private Integer idDocente;

    @NotNull(message = "El curso es obligatorio.")
    private Integer idCurso;

    @NotNull(message = "El grado es obligatorio.")
    private Integer idGrado;

    @NotNull(message = "El período académico es obligatorio.")
    private Integer idPeriodoAcademico;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de asignación es obligatoria.")
    private LocalDate fechaAsignacion;

    @NotNull(message = "Debe indicar si el docente es titular.")
    private Boolean esTitular;

    private String estadoAsignacion;
    private String observaciones;
}
