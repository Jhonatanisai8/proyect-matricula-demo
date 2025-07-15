package com.isai.demowebregistrationsystem.model.dtos.horarios;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HorarioListadoDTO {

    private Integer idHorario;

    private String diaSemana;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private String nombreCurso;

    private String nombreDocente;

    private String nombreGrado;

    private String nombrePeriodoAcademico;

    private String nombreSalon;

    private String nombreSeccion;


    private Boolean activo;

}
