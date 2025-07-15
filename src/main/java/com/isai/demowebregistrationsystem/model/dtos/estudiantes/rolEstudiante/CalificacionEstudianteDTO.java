package com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para representar una calificación individual en la vista de "Mis Notas" del estudiante.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalificacionEstudianteDTO {

    private Integer idCalificacion;

    private String nombreCurso;

    private String tipoEvaluacion;

    private BigDecimal nota;

    private BigDecimal pesoPorcentual;

    private LocalDate fechaEvaluacion;

    private String observaciones;
}
