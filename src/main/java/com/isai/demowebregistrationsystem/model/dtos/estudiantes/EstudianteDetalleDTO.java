package com.isai.demowebregistrationsystem.model.dtos.estudiantes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteDetalleDTO {


    // Info de Persona
    private Integer idPersona;
    private String nombres;
    private String apellidos;
    private String dni;
    private String direccion;
    private String emailPersonal;
    private String estadoCivil;
    private LocalDate fechaNacimiento;
    private String fotoUrl;
    private MultipartFile foto;

    private String genero;
    private String telefono;
    private String tipoDocumento;
    private Boolean personaActivo;
    private LocalDateTime fechaRegistroPersona;
    private LocalDateTime fechaActualizacionPersona;

    // Info de Estudiante
    private Integer idEstudiante;
    private String codigoEstudiante;
    private String emailEducativo;
    private String institucionProcedencia;
    private String gradoAnterior;
    private Boolean seguroEscolar;
    private String alergias;
    private String observacionesMedicas;
    private String contactoEmergencia;
    private String telefonoEmergencia;
    private String tipoSangre;

    // Info del Apoderado principal (si existe)
    private Integer idApoderadoPrincipal;
    private String apoderadoNombresCompletos;
    private String apoderadoParentesco;
    private String apoderadoTelefonoContacto;
    private String apoderadoEmailPersonal;

    // Info de la Matricula actual (simplificada)
    private Integer idMatriculaActual;
    private String numeroMatriculaActual;
    private String estadoMatriculaActual;
    private LocalDate fechaMatriculaActual;
    private String gradoMatriculado;
    private String seccionMatriculada;
    private String periodoAcademicoMatriculado;
}
