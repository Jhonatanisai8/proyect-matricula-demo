package com.isai.demowebregistrationsystem.model.dtos.docente;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotaIndividualRequestDTO {

    @NotNull(message = "El ID de estudiante no puede ser nulo.")
    private Integer idEstudiante;

    @NotNull(message = "La nota no puede ser nula.")
    @DecimalMin(value = "0.0", message = "La nota mínima es 0.0")
    @DecimalMax(value = "20.0", message = "La nota máxima es 20.0")
    private BigDecimal nota;

    private Integer idCalificacion;
}
