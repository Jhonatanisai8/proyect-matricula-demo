package com.isai.demowebregistrationsystem.model.dtos.docente;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MisEstudiantesViewDTO {

    private List<CursoDocenteDTO> cursosAsignados;

}
