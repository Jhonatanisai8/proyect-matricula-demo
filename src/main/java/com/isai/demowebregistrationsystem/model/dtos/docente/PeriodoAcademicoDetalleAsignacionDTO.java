package com.isai.demowebregistrationsystem.model.dtos.docente;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PeriodoAcademicoDetalleAsignacionDTO {

    private Integer idPeriodo;

    private String nombrePeriodo;

    private Integer anoAcademico;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String estado;
}
