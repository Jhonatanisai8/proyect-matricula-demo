package com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para agrupar las calificaciones por curso y calcular el promedio.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CursoNotasEstudianteDTO {

    private Integer idCurso;

    private String nombreCurso;

    private String codigoCurso;

    private String nombreDocente;

    private List<CalificacionEstudianteDTO> calificaciones;

    private BigDecimal promedioCurso;
}
