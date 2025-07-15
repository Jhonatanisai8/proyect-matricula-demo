package com.isai.demowebregistrationsystem.model.dtos.docente;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteNotaDTO {

    private Integer idEstudiante;

    private String codigoEstudiante;

    private String nombresCompletos;

    private String dni;

    private String nombreSeccion;

    private BigDecimal notaActual;

    private Integer idCalificacion;
}
