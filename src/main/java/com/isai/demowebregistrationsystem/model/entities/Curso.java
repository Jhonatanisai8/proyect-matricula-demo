package com.isai.demowebregistrationsystem.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CURSO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Integer idCurso;

    @Column(name = "codigo_curso", unique = true, nullable = false, length = 20)
    private String codigoCurso;

    @Column(name = "nombre_curso", nullable = false, length = 100)
    private String nombreCurso;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "horas_semanales")
    private Integer horasSemanales;

    @Column(name = "creditos")
    private Integer creditos;

    @Column(name = "area_conocimiento", length = 100)
    private String areaConocimiento;

    @Column(name = "es_obligatorio", nullable = false)
    private Boolean esObligatorio;

    @Column(name = "prerequisitos", length = 255)
    private String prerequisitos;

    @Column(name = "competencias", columnDefinition = "TEXT")
    private String competencias;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grado", nullable = false)
    private Grado grado;

    @PrePersist
    protected void onCreate() {
        if (this.esObligatorio == null) this.esObligatorio = true;
        if (this.activo == null) this.activo = true;
    }
}