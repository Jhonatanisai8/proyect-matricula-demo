package com.isai.demowebregistrationsystem.model.dtos.docente;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstudianteBasicoDTO {

    private Integer idEstudiante;

    private String codigoEstudiante;

    private String nombresCompletos;

    private String dni;

    private String estadoMatricula;

    private String nombreSeccion;
}
