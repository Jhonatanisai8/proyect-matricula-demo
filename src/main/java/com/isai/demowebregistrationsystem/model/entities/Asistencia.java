package com.isai.demowebregistrationsystem.model.entities;

import com.isai.demowebregistrationsystem.model.enums.EstadoAsistencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "ASISTENCIA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asistencia")
    private Integer idAsistencia;

    @Column(name = "fecha_asistencia", nullable = false)
    private LocalDate fechaAsistencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 50)
    private EstadoAsistencia estado;

    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @Column(name = "hora_llegada")
    private LocalTime horaLlegada;

    @Column(name = "justificada", nullable = false)
    private Boolean justificada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_horario", nullable = false)
    private Horario horario;

    @PrePersist
    protected void onCreate() {
        if (this.justificada == null) this.justificada = false;
    }
}