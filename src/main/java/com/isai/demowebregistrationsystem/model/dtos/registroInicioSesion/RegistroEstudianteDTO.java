package com.isai.demowebregistrationsystem.model.dtos.registroInicioSesion;

import com.isai.demowebregistrationsystem.model.enums.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
public class RegistroEstudianteDTO
        extends RegistroUsuarioDTO {

    @NotBlank(message = "El código de estudiante es obligatorio.")
    @Size(max = 50, message = "El código de estudiante no puede exceder los 50 caracteres.")
    private String codigoEstudiante;

    @Size(max = 255, message = "Las alergias no pueden exceder los 255 caracteres.")
    private String alergias;

    @Size(max = 100, message = "El contacto de emergencia no puede exceder los 100 caracteres.")
    private String contactoEmergencia;

    @Size(max = 100, message = "El email educativo no puede exceder los 100 caracteres.")
    private String emailEducativo;

    @Size(max = 100, message = "El grado anterior no puede exceder los 100 caracteres.")
    private String gradoAnterior;

    @Size(max = 255, message = "La institución de procedencia no puede exceder los 255 caracteres.")
    private String institucionProcedencia;

    private String observacionesMedicas;

    private Boolean seguroEscolar;

    @Size(max = 20, message = "El teléfono de emergencia no puede exceder los 20 caracteres.")
    private String telefonoEmergencia;

    @Size(max = 10, message = "El tipo de sangre no puede exceder los 10 caracteres.")
    private String tipoSangre;

    @Length(max = 8)
    private String dniApoderadoPrin;

    public RegistroEstudianteDTO() {
        this.setRol(Rol.ESTUDIANTE);
        this.seguroEscolar = false;
    }
}
