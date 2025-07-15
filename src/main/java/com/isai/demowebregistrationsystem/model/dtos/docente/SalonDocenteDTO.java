package com.isai.demowebregistrationsystem.model.dtos.docente;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalonDocenteDTO {

    private Integer idSalon;

    private String nombreSalon;

    private String codigoSalon;

    private String ubicacion;

    private Integer capacidadMaxima;
}
