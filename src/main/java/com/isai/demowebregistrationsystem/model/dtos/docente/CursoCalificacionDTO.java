package com.isai.demowebregistrationsystem.model.dtos.docente;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CursoCalificacionDTO {

    private Integer idAsignacion;

    private String nombreCurso;

    private String codigoCurso;

    private String nombreGrado;

    private String nombrePeriodoAcademico;

    private Integer idCurso;

    private Integer idGrado;

    private Integer idPeriodoAcademico;
}
