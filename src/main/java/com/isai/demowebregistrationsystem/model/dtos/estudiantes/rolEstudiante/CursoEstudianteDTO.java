package com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante;

import lombok.*;

import java.time.LocalTime;

/**
 * DTO para representar un curso en la lista de "Mis Cursos" del estudiante.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CursoEstudianteDTO {

    private Integer idCurso;

    private String nombreCurso;

    private String codigoCurso;

    private String areaConocimiento;

    private String descripcion;

    private Integer creditos;

    private Integer horasSemanales;

    private String nombreDocente;

    private String diaSemana;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private String nombreSalon;
}
