package com.isai.demowebregistrationsystem.model.dtos.estudiantes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteListadoDTO {

    private Integer idEstudiante;

    private Integer idPersona;

    private String codigoEstudiante;

    private String nombresCompletos;

    private String dni;

    private String emailPersonal;

    private String telefono;

    private LocalDate fechaNacimiento;

    private Boolean activo;

    private String gradoActual;

    private String seccionActual;
}
