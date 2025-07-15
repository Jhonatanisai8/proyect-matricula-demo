package com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante;

import lombok.*;

import java.util.List;

/**
 * DTO para la vista de "Mis Cursos" del estudiante.
 * Contiene la lista de cursos en los que el estudiante está matriculado.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CursosEstudianteViewDTO {

    private String nombreEstudiante;

    private String gradoActual;

    private String seccionActual;

    private String periodoAcademicoActual;

    private List<CursoEstudianteDTO> cursosMatriculados;

    private String mensajeSinCursos;
}
