package com.isai.demowebregistrationsystem.model.dtos.secciones;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeccionDetalleDTO {

    private Integer idSeccion;

    private String nombreSeccion;

    private Integer idDocenteTutor;

    private String nombreDocenteTutor;

    private Integer idGrado;

    private String nombreGrado;

    private Integer idPeriodoAcademico;

    private String nombrePeriodoAcademico;

}
