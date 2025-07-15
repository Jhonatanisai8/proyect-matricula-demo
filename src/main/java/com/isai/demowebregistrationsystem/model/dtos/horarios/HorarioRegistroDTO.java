package com.isai.demowebregistrationsystem.model.dtos.horarios;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class HorarioRegistroDTO {

    private Integer idHorario;

    @NotNull(message = "El estado activo es obligatorio.")
    private Boolean activo;

    @NotBlank(message = "El día de la semana es obligatorio.")
    @Size(max = 20, message = "El día de la semana no puede exceder 20 caracteres.")
    private String diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria.")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria.")
    private LocalTime horaFin;

    @Size(max = 255, message = "Las observaciones no pueden exceder 255 caracteres.")
    private String observaciones;

    @Size(max = 50, message = "El tipo de clase no puede exceder 50 caracteres.")
    private String tipoClase;

    @NotNull(message = "El curso es obligatorio.")
    private Integer idCurso;

    @NotNull(message = "El docente es obligatorio.")
    private Integer idDocente;

    @NotNull(message = "El grado es obligatorio.")
    private Integer idGrado;

    @NotNull(message = "El período académico es obligatorio.")
    private Integer idPeriodoAcademico;

    @NotNull(message = "El salón es obligatorio.")
    private Integer idSalon;

    @NotNull(message = "La sección es obligatoria.")
    private Integer idSeccion;

    public HorarioRegistroDTO() {
        this.activo = true;
    }
}

