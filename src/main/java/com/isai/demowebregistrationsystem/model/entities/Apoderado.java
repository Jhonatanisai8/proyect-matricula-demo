package com.isai.demowebregistrationsystem.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "APODERADO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apoderado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_apoderado")
    private Integer idApoderado;

    @Column(name = "ocupacion", length = 100)
    private String ocupacion;

    @Column(name = "lugar_trabajo", length = 255)
    private String lugarTrabajo;

    @Column(name = "telefono_trabajo", length = 9)
    private String telefonoTrabajo;

    @Column(name = "parentesco", length = 50)
    private String parentesco;

    @Column(name = "nivel_educativo", length = 50)
    private String nivelEducativo;

    @Column(name = "ingreso_mensual", precision = 10, scale = 2)
    private BigDecimal ingresoMensual;

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal;

    @Column(name = "autorizado_recoger", nullable = false)
    private Boolean autorizadoRecoger;

    @Column(name = "referencia_personal", length = 255)
    private String referenciaPersonal;

    @Column(name = "telefono_referencia", length = 20)
    private String telefonoReferencia;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", unique = true, nullable = false)
    private Persona persona;

    @PrePersist
    protected void onCreate() {
        if (this.esPrincipal == null) this.esPrincipal = false;
        if (this.autorizadoRecoger == null) this.autorizadoRecoger = false;
    }
}