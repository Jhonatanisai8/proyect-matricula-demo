package com.isai.demowebregistrationsystem.model.dtos.docente;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradoDetalleAsignacionDTO {

    private Integer idGrado;

    private String nombreGrado;

    private String codigoGrado;

    private String descripcion;

    private Integer nivel;

}
