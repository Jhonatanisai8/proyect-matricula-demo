package com.isai.demowebregistrationsystem.model.dtos.apoderado;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardApoderadpDTO {
    private String nombresCompletos;
    private String dni;
    private String emailPersonal;
    private String telefono;
    private String ocupacion;
    private String nivelEducativo;
    private Integer totalHijosVinculados;
}
