package com.isai.demowebregistrationsystem.model.dtos.docente;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HorarioAsistenciaDTO {

    private Integer idHorario;

    private String diaSemana;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private String nombreCurso;

    private String nombreGrado;

    private String nombreSeccion;

    private String nombrePeriodoAcademico;

    private Integer idGrado;

    private Integer idPeriodoAcademico;
}
