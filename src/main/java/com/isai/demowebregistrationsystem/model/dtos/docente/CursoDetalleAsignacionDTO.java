package com.isai.demowebregistrationsystem.model.dtos.docente;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CursoDetalleAsignacionDTO {

    private Integer idCurso;

    private String codigoCurso;

    private String nombreCurso;

    private String descripcion;

    private String areaConocimiento;

    private Integer creditos;

    private Integer horasSemanales;

    private Boolean esObligatorio;

    private String prerequisitos;

    private String competencias;
}
