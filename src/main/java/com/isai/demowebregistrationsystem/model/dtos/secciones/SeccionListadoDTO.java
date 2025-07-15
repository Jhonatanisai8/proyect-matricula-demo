package com.isai.demowebregistrationsystem.model.dtos.secciones;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeccionListadoDTO {

    private Integer idSeccion;

    private String nombreSeccion;

    private String nombreGrado;

    private String nombrePeriodoAcademico;

    private String nombreDocenteTutor;

}
