package com.isai.demowebregistrationsystem.model.dtos.docente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistroNotasRequestDTO {

    @NotNull(message = "El ID de asignación no puede ser nulo.")
    private Integer idAsignacion;

    @Valid
    @NotNull(message = "La lista de notas no puede ser nula.")
    private List<NotaIndividualRequestDTO> notas;
}
