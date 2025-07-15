package com.isai.demowebregistrationsystem.model.dtos.docente;


import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AsignacionDocenteDetalleDTO {


    private Integer idAsignacion;

    private LocalDate fechaAsignacion;

    private Boolean esTitular;

    private String estadoAsignacion;

    private String observaciones;

    private CursoDetalleAsignacionDTO curso;

    private GradoDetalleAsignacionDTO grado;

    private PeriodoAcademicoDetalleAsignacionDTO periodoAcademico;

    private List<HorarioAsignacionDTO> horarios;

    private List<EstudianteBasicoDTO> estudiantes;

    private String nombreDocente;
}
