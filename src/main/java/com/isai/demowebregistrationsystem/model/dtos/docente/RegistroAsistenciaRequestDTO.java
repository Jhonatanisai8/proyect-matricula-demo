package com.isai.demowebregistrationsystem.model.dtos.docente;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistroAsistenciaRequestDTO {

    @NotNull(message = "El ID de horario no puede ser nulo.")
    private Integer idHorario;

    @NotNull(message = "La fecha de asistencia no puede ser nula.")
    private LocalDate fechaAsistencia;

    @Valid
    @NotNull(message = "La lista de asistencias no puede ser nula.")
    private List<AsistenciaIndividualRequestDTO> asistencias;
}
