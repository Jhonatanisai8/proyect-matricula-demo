package com.isai.demowebregistrationsystem.model.dtos.estudiantes;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class MatriculaGestionDTO {
    private Integer idMatricula;
    @NotNull(message = "El ID del estudiante es obligatorio")
    private Integer idEstudiante;

    @NotBlank(message = "Número de matrícula es obligatorio")
    @Size(max = 50, message = "Número de matrícula no puede exceder 50 caracteres")
    private String numeroMatricula;

    @NotNull(message = "Fecha de matrícula es obligatoria")
    @PastOrPresent(message = "Fecha de matrícula no puede ser en el futuro")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaMatricula;

    @NotBlank(message = "Estado de matrícula es obligatorio")
    @Size(max = 50, message = "Estado de matrícula no puede exceder 50 caracteres")
    private String estadoMatricula; // Ej: "REGISTRADA", "PENDIENTE_PAGO", "COMPLETADA"

    @NotNull(message = "Monto de matrícula es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "Monto de matrícula debe ser no negativo")
    @Digits(integer = 10, fraction = 2, message = "Monto de matrícula debe tener hasta 10 dígitos y 2 decimales")
    private BigDecimal montoMatricula;

    @NotNull(message = "Monto de pensión es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "Monto de pensión debe ser no negativo")
    @Digits(integer = 10, fraction = 2, message = "Monto de pensión debe tener hasta 10 dígitos y 2 decimales")
    private BigDecimal montoPension;

    @Size(max = 50, message = "Modalidad de pago no puede exceder 50 caracteres")
    private String modalidadPago;

    @NotNull(message = "Documentos completos es obligatorio")
    private Boolean documentosCompletos;

    @Size(max = 255, message = "Documentos pendientes no puede exceder 255 caracteres")
    private String documentosPendientes;

    @Size(max = 1000, message = "Observaciones no pueden exceder 1000 caracteres") // TEXT en DB
    private String observaciones;

    // Relaciones
    @NotNull(message = "Grado es obligatorio")
    private Integer idGrado;

    @NotNull(message = "Sección es obligatoria")
    private Integer idSeccion;

    @NotNull(message = "Período académico es obligatorio")
    private Integer idPeriodoAcademico;

    @NotNull(message = "Apoderado que realiza la matrícula es obligatorio")
    private Integer idApoderadoRealiza;

    // Constructor vacío
    public MatriculaGestionDTO() {
        this.documentosCompletos = false;
        this.montoMatricula = BigDecimal.ZERO;
        this.montoPension = BigDecimal.ZERO;
    }
}
