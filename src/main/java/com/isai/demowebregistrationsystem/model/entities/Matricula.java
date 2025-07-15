package com.isai.demowebregistrationsystem.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "MATRICULA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_matricula")
    private Integer idMatricula;

    @Column(name = "numero_matricula", unique = true, nullable = false, length = 50)
    private String numeroMatricula;

    @Column(name = "fecha_matricula", nullable = false)
    private LocalDate fechaMatricula;

    @Column(name = "estado_matricula", nullable = false, length = 50)
    private String estadoMatricula; // Usaremos este campo para determinar si está "activa"

    @Column(name = "monto_matricula", precision = 10, scale = 2)
    private BigDecimal montoMatricula;

    @Column(name = "monto_pension", precision = 10, scale = 2)
    private BigDecimal montoPension;

    @Column(name = "modalidad_pago", length = 50)
    private String modalidadPago;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "documentos_pendientes", length = 255)
    private String documentosPendientes;

    @Column(name = "documentos_completos", nullable = false)
    private Boolean documentosCompletos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seccion", nullable = false)
    private Seccion seccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grado", nullable = false)
    private Grado grado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_periodo_academico", nullable = false)
    private PeriodoAcademico periodoAcademico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_apoderado_realiza", nullable = false)
    private Apoderado apoderadoRealiza;

    @PrePersist
    protected void onCreate() {
        if (this.fechaMatricula == null) this.fechaMatricula = LocalDate.now();
        if (this.documentosCompletos == null) this.documentosCompletos = false;
    }
}