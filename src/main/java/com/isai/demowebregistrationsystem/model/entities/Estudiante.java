package com.isai.demowebregistrationsystem.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ESTUDIANTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private Integer idEstudiante;

    @Column(name = "codigo_estudiante", unique = true, nullable = false, length = 50)
    private String codigoEstudiante;

    @Column(name = "email_educativo", length = 100)
    private String emailEducativo;

    @Column(name = "grado_anterior", length = 100)
    private String gradoAnterior;

    @Column(name = "institucion_procedencia", length = 255)
    private String institucionProcedencia;

    @Column(name = "tipo_sangre", length = 10)
    private String tipoSangre;

    @Column(name = "observaciones_medicas", columnDefinition = "TEXT")
    private String observacionesMedicas;

    @Column(name = "alergias", length = 255)
    private String alergias;

    @Column(name = "contacto_emergencia", length = 100)
    private String contactoEmergencia;

    @Column(name = "telefono_emergencia", length = 20)
    private String telefonoEmergencia;

    @Column(name = "seguro_escolar", nullable = false)
    private Boolean seguroEscolar;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", unique = true, nullable = false)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_apoderado", referencedColumnName = "id_apoderado") // Relación con Apoderado
    private Apoderado apoderadoPrincipal; // Nombre del campo para el apoderado principal

    @PrePersist
    protected void onCreate() {
        if (this.seguroEscolar == null) this.seguroEscolar = false;
    }
}