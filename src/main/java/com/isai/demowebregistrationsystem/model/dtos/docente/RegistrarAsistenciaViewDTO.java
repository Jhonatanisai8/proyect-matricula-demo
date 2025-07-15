package com.isai.demowebregistrationsystem.model.dtos.docente;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrarAsistenciaViewDTO {

    private List<HorarioAsistenciaDTO> horariosParaAsistencia;

}
