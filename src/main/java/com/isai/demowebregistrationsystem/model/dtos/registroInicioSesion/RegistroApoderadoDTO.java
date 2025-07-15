package com.isai.demowebregistrationsystem.model.dtos.registroInicioSesion;

import com.isai.demowebregistrationsystem.model.enums.Rol;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class RegistroApoderadoDTO
        extends RegistroUsuarioDTO {

    private Boolean autorizadoRecoger;
    private Boolean esPrincipal;

    @DecimalMin(value = "0.0", inclusive = true, message = "El ingreso mensual no puede ser negativo.")
    private BigDecimal ingresoMensual;

    @Size(max = 255, message = "El lugar de trabajo no puede exceder los 255 caracteres.")
    private String lugarTrabajo;

    @Size(max = 50, message = "El nivel educativo no puede exceder los 50 caracteres.")
    private String nivelEducativo;

    @Size(max = 100, message = "La ocupación no puede exceder los 100 caracteres.")
    private String ocupacion;

    @NotBlank(message = "El parentesco es obligatorio.")
    @Size(max = 50, message = "El parentesco no puede exceder los 50 caracteres.")
    private String parentesco;

    @Size(max = 255, message = "La referencia personal no puede exceder los 255 caracteres.")
    private String referenciaPersonal;

    @Size(max = 20, message = "El teléfono de referencia no puede exceder los 20 caracteres.")
    private String telefonoReferencia;

    @Size(max = 20, message = "El teléfono de trabajo no puede exceder los 20 caracteres.")
    private String telefonoTrabajo;

    public RegistroApoderadoDTO() {
        this.setRol(Rol.APODERADO);
        this.autorizadoRecoger = false;
        this.esPrincipal = true;
    }
}
