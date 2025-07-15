package com.isai.demowebregistrationsystem.model.dtos.estudiantes.rolEstudiante;

import lombok.*;

import java.time.LocalDate;

/**
 * DTO para el panel principal del estudiante.
 * Contiene información resumida del estudiante y su matrícula actual.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstudianteDashboardDTO {

    private String nombresCompletos;

    private String codigoEstudiante;

    private String emailEducativo;

    private LocalDate fechaNacimiento;

    private String gradoActual;

    private String seccionActual;

    private String periodoAcademicoActual;

    private String numeroMatriculaActual;

    private String fotoUrl;

    private String contactoEmergencia;

    private String telefonoEmergencia;

    private String tipoSangre;

    private Boolean seguroEscolar;

    private String apoderadoPrincipal;

    private String telefonoApoderadoPrincipal;

}
