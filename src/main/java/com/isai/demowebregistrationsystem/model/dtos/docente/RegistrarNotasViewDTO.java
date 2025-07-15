package com.isai.demowebregistrationsystem.model.dtos.docente;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarNotasViewDTO {

    private List<CursoCalificacionDTO> cursosParaNotas;

}
