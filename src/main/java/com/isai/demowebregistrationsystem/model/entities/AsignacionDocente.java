package com.isai.demowebregistrationsystem.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "ASIGNACION_DOCENTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignacionDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    private Integer idAsignacion;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate fechaAsignacion;

    @Column(name = "estado_asignacion", length = 50)
    private String estadoAsignacion;

    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @Column(name = "es_titular", nullable = false)
    private Boolean esTitular;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_docente", nullable = false)
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grado", nullable = false)
    private Grado grado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_periodo_academico", nullable = false)
    private PeriodoAcademico periodoAcademico;

    @PrePersist
    protected void onCreate() {
        if (this.fechaAsignacion == null) this.fechaAsignacion = LocalDate.now();
        if (this.esTitular == null) this.esTitular = false;
    }
}