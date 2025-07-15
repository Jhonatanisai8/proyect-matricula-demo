package com.isai.demowebregistrationsystem.model.dtos.apoderado;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudianteListaApoderadoDTO {

    private Integer idEstudiante;

    private Integer idMatriculaActual;

    private String codigoEstudiante;

    private String nombresCompletos;

    private String dni;

    private LocalDate fechaNacimiento;

    private String emailEducativo;

    private String telefonoEmergencia;

    private Boolean seguroEscolar;

    private String parentescoConApoderadoPrincipal;
}
