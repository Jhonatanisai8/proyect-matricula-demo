package com.isai.demowebregistrationsystem.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatriculaDetalleDTO {

    private Integer id;

    private String numeroMatricula;

    private LocalDate fechaMatricula;

    private String estadoMatricula;

    private String documentosPendientes;

    private String modalidadPago;

    private BigDecimal montoMatricula;

    private BigDecimal montoPension;

    private String observaciones;

    private String nombreCompletoEstudiante;

    private String codigoEstudiante;

    private String nombreGrado;

    private String nombreSeccion;

    private String nombrePeriodoAcademico;

    private String nombreApoderadoRealiza;

    private String dniApoderadoRealiza;
}
