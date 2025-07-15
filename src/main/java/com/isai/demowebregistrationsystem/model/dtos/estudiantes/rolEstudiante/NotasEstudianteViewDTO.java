package com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante;


import lombok.*;

import java.util.List;

/**
 * DTO principal para la vista de "Mis Notas" del estudiante.
 * Contiene la información general del estudiante y la lista de cursos con sus notas.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotasEstudianteViewDTO {

    private String nombreEstudiante;

    private String gradoActual;

    private String seccionActual;

    private String periodoAcademicoActual;

    private List<CursoNotasEstudianteDTO> cursosConNotas;

    private String mensajeSinNotas;
}
