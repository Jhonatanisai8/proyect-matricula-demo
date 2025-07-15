package com.isai.demowebregistrationsystem.model.dtos.docente;

import com.isai.demowebregistrationsystem.model.dtos.secciones.SeccionDetalleDTO;
import lombok.*;

import java.time.LocalTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HorarioAsignacionDTO {

    private Integer idHorario;

    private String diaSemana;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private String tipoClase;

    private String observaciones;


    private SalonDocenteDTO salon;


    private SeccionDetalleDTO seccion;
}
