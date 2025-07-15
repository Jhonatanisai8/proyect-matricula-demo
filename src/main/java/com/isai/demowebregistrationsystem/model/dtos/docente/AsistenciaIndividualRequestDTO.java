package com.isai.demowebregistrationsystem.model.dtos.docente;

import com.isai.demowebregistrationsystem.model.enums.EstadoAsistencia;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AsistenciaIndividualRequestDTO {

    @NotNull(message = "El ID de estudiante no puede ser nulo.")
    private Integer idEstudiante;

    @NotNull(message = "El estado de asistencia no puede estar vacío.")
    private EstadoAsistencia estadoAsistencia;

    @NotNull(message = "El campo justificada no puede ser nulo.")
    private Boolean justificada;

    private String observaciones;
}
