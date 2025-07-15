package com.isai.demowebregistrationsystem.model.dtos.docente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CursoAsignadoDTO {

    private Integer idAsignacion;

    private Integer idCurso;

    private String nombreCurso;

    private String codigoCurso;

    private Integer idPeriodoAcademico;

    private String nombrePeriodoAcademico;

    private Integer anoAcademico;

    private String nombreGrado;

    private String estadoAsignacion;

}
