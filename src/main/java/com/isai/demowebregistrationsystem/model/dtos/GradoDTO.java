package com.isai.demowebregistrationsystem.model.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GradoDTO {
    private Integer idGrado;

    @NotBlank(message = "El nombre del grado es obligatorio.")
    @Size(max = 100, message = "El nombre del grado no puede exceder los 100 caracteres.")
    private String nombreGrado;

    //    @NotBlank(message = "El código del grado es obligatorio.")
//    @Size(max = 20, message = "El código del grado no puede exceder los 20 caracteres.")
    private String codigoGrado;

    @Min(value = 0, message = "El nivel no puede ser negativo.")
    private Integer nivel;

    @Min(value = 0, message = "La edad mínima no puede ser negativa.")
    private Integer edadMinima;

    @Min(value = 0, message = "La edad máxima no puede ser negativa.")
    private Integer edadMaxima;

    @Min(value = 0, message = "Los cupos disponibles no pueden ser negativos.")
    private Integer cuposDisponibles;

//    @DecimalMin(value = "0.00", message = "La pensión mensual no puede ser negativa.")
//    @Digits(integer = 8, fraction = 2, message = "La pensión mensual debe tener hasta 8 dígitos enteros y 2 decimales.")
//    private BigDecimal pensionMensual;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres.")
    private String descripcion;

    private Boolean activo;

    @Min(value = 0, message = "El orden no puede ser negativo.")
    private Integer orden;
}
