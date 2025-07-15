package com.isai.demowebregistrationsystem.model.dtos.docente;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CursoDocenteDTO {

    private Integer idAsignacion;

    private String nombreCurso;

    private String codigoCurso;

    private String nombreGrado;

    private String nombrePeriodoAcademico;

}
