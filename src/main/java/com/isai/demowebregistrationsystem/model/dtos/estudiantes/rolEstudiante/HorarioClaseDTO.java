package com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante;

import lombok.*;

import java.time.LocalTime;

/**
 * DTO para representar una entrada de clase individual en el horario del estudiante.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HorarioClaseDTO {

    private Integer idHorario;

    private String diaSemana;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private String nombreCurso;

    private String codigoCurso;

    private String nombreDocente;

    private String nombreSalon;
}
