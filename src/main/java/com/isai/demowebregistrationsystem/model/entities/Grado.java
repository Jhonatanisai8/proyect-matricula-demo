package com.isai.demowebregistrationsystem.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "GRADO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grado")
    private Integer idGrado;

    @Column(name = "codigo_grado", unique = true, nullable = false, length = 20)
    private String codigoGrado;

    @Column(name = "nombre_grado", nullable = false, length = 100)
    private String nombreGrado;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "nivel")
    private Integer nivel;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "edad_minima")
    private Integer edadMinima;

    @Column(name = "edad_maxima")
    private Integer edadMaxima;

    @Column(name = "cupos_disponibles")
    private Integer cuposDisponibles;

//    @Column(name = "pension_mensual", precision = 10, scale = 2)
//    private BigDecimal pensionMensual;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @PrePersist
    protected void onCreate() {
        if (this.activo == null) this.activo = true;
    }
}