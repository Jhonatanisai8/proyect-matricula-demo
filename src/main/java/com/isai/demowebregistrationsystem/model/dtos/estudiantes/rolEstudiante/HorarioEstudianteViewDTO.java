package com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * DTO principal para la vista de "Mi Horario" del estudiante.
 * Contiene la información general del estudiante y el horario agrupado por día.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HorarioEstudianteViewDTO {

    private String nombreEstudiante;

    private String gradoActual;

    private String seccionActual;

    private String periodoAcademicoActual;

    private Map<String, List<HorarioClaseDTO>> horarioPorDia;

    private List<String> diasOrdenados;

    private String mensajeSinHorario;

    private Map<String, Map<String, HorarioClaseDTO>> horarioPorDiaYHora;

    public HorarioClaseDTO getClasePara(String dia, String hora) {
        if (horarioPorDiaYHora == null || !horarioPorDiaYHora.containsKey(dia)) return null;
        Map<String, HorarioClaseDTO> porHora = horarioPorDiaYHora.get(dia);
        return porHora != null ? porHora.get(hora) : null;
    }

}
