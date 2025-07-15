package com.isai.demowebregistrationsystem.model.dtos.docente;


import com.isai.demowebregistrationsystem.model.enums.EstadoAsistencia;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstudianteAsistenciaDTO {

    private Integer idEstudiante;

    private String codigoEstudiante;

    private String nombresCompletos;

    private String dni;

    private String nombreSeccion;

    private EstadoAsistencia estadoAsistenciaActual;

    private Boolean justificadaActual;

    private String observacionesActual;

    private Integer idAsistencia;
}
