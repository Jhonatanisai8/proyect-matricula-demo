package com.isai.demowebregistrationsystem.model.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SalonDTO {

    private Integer idSalon;

    @NotBlank(message = "El código del salón es obligatorio.")
    @Size(max = 20, message = "El código no puede exceder los 20 caracteres.")
    private String codigoSalon;

    @NotBlank(message = "El nombre del salón es obligatorio.")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    private String nombreSalon;

    @NotNull(message = "La capacidad máxima es obligatoria.")
    @Min(value = 1, message = "La capacidad máxima debe ser al menos 1.")
    private Integer capacidadMaxima;

    @Size(max = 100, message = "La ubicación no puede exceder los 100 caracteres.")
    private String ubicacion;

    @Size(max = 50, message = "El piso no puede exceder los 50 caracteres.")
    private String piso;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres.")
    private String descripcion;

    @Size(max = 255, message = "Los recursos disponibles no pueden exceder los 255 caracteres.")
    private String recursosDisponibles;

    @NotNull(message = "Debe especificar si tiene proyector.")
    private Boolean tieneProyector;

    @NotNull(message = "Debe especificar si tiene aire acondicionado.")
    private Boolean tieneAireAcondicionado;

    private Boolean activo;
}
