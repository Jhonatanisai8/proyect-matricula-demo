package com.isai.demowebregistrationsystem.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SALON")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_salon")
    private Integer idSalon;

    @Column(name = "codigo_salon", unique = true, nullable = false, length = 20)
    private String codigoSalon;

    @Column(name = "nombre_salon", nullable = false, length = 100)
    private String nombreSalon;

    @Column(name = "capacidad_maxima")
    private Integer capacidadMaxima;

    @Column(name = "ubicacion", length = 100)
    private String ubicacion;

    @Column(name = "piso", length = 50)
    private String piso;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "recursos_disponibles", length = 255)
    private String recursosDisponibles;

    @Column(name = "tiene_proyector", nullable = false)
    private Boolean tieneProyector;

    @Column(name = "tiene_aire_acondicionado", nullable = false)
    private Boolean tieneAireAcondicionado;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @PrePersist
    protected void onCreate() {
        if (this.tieneProyector == null) this.tieneProyector = false;
        if (this.tieneAireAcondicionado == null) this.tieneAireAcondicionado = false;
        if (this.activo == null) this.activo = true;
    }
}