package com.isai.demowebregistrationsystem.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "DOCENTE")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_docente")
    private Integer idDocente;

    @Column(name = "codigo_docente", unique = true, nullable = false, length = 50)
    private String codigoDocente;

    @Column(name = "email_institucional", length = 100)
    private String emailInstitucional;

    @Column(name = "especialidad_principal", length = 100)
    private String especialidadPrincipal;

    @Column(name = "especialidad_secundaria", length = 100)
    private String especialidadSecundaria;

    @Column(name = "titulo_profesional", length = 100)
    private String tituloProfesional;

    @Column(name = "universidad_egreso", length = 255)
    private String universidadEgreso;

    @Column(name = "fecha_contratacion")
    private LocalDate fechaContratacion;

    @Column(name = "salario_base", precision = 10, scale = 2)
    private BigDecimal salarioBase;

    @Column(name = "tipo_contrato", length = 50)
    private String tipoContrato;

    @Column(name = "estado_laboral", length = 50)
    private String estadoLaboral;

    @Column(name = "anos_experiencia")
    private Integer anosExperiencia;

    @Column(name = "cv_url", length = 255)
    private String cvUrl;

    @Column(name = "coordinador", nullable = false)
    private Boolean coordinador;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", unique = true, nullable = false)
    private Persona persona;

    @PrePersist
    protected void onCreate() {
        if (this.coordinador == null) this.coordinador = false;
    }

    @Column(name = "activo")
    private Boolean activo;
}