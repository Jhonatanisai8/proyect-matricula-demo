package com.isai.demowebregistrationsystem.model.dtos.horarios;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HorarioDetalleDTO {

    private Integer idHorario;

    private Boolean activo;

    private String diaSemana;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private String observaciones;

    private String tipoClase;

    private Integer idCurso;

    private String nombreCurso;

    private Integer idDocente;

    private String nombreDocente;

    private Integer idGrado;

    private String nombreGrado;

    private Integer idPeriodoAcademico;

    private String nombrePeriodoAcademico;

    private Integer idSalon;

    private String nombreSalon;

    private Integer idSeccion;

    private String nombreSeccion;

}
