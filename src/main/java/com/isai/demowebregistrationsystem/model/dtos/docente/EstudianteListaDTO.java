package com.isai.demowebregistrationsystem.model.dtos.docente;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteListaDTO {

    private Integer idEstudiante;

    private String codigoEstudiante;

    private String nombresCompletos;

    private String dni;

    private String nombreSeccion;

    private String estadoMatricula;

}
