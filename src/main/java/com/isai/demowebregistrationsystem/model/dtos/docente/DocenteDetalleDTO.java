package com.isai.demowebregistrationsystem.model.dtos.docente;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocenteDetalleDTO {

    private Integer idPersona;
    private String dni;
    private String nombresCompletos;
    private LocalDate fechaNacimiento;
    private String genero;
    private String direccion;
    private String telefono;
    private String emailPersonal;
    private String estadoCivil;
    private String tipoDocumento;
    private String fotoUrlPersona;
    private MultipartFile foto;

    private Integer idDocente;
    private String codigoDocente;
    private String emailInstitucional;
    private String especialidadPrincipal;
    private String especialidadSecundaria;
    private String tituloProfesional;
    private String universidadEgreso;
    private LocalDate fechaContratacion;
    private BigDecimal salarioBase;
    private String tipoContrato;
    private String estadoLaboral;
    private Integer anosExperiencia;
    private String cvUrl;
    private Boolean coordinador;


    private String username;
    private Boolean usuarioActivo;
}
