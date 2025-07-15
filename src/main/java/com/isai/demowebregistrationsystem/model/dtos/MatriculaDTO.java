package com.isai.demowebregistrationsystem.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatriculaDTO {

    private Integer id;

    private String numeroMatricula;

    private LocalDate fechaMatricula;

    private String estadoMatricula;

    private String nombreEstudiante;

    private String apellidoEstudiante;

    private String nombreGrado;

    private String nombrePeriodoAcademico;

    private String nombreApoderadoRealiza;
}
