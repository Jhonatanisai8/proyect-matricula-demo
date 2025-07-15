package com.isai.demowebregistrationsystem.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {
    private long totalUsuarios;
    private long totalDocentes;
    private long totalApoderados;
    private long totalEstudiantes;
}