package com.isai.demowebregistrationsystem.model.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApoderadoDTO {

    private Integer idApoderado;

    private Integer idPersona;

    @NotBlank(message = "El DNI no puede estar vacío.")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos.")
    private String dni;

    @NotBlank(message = "Los nombres no pueden estar vacíos.")
    @Size(max = 100, message = "Los nombres no pueden exceder los 100 caracteres.")
    private String nombres;

    @NotBlank(message = "Los apellidos no pueden estar vacíos.")
    @Size(max = 100, message = "Los apellidos no pueden exceder los 100 caracteres.")
    private String apellidos;

    @NotNull(message = "La fecha de nacimiento no puede estar vacía.")
    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El género no puede estar vacío.")
    private String genero;

    @Size(max = 255, message = "La dirección no puede exceder los 255 caracteres.")
    private String direccion;

    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres.")
    private String telefono;

    @Email(message = "El formato del email personal no es válido.")
    @Size(max = 100, message = "El email personal no puede exceder los 100 caracteres.")
    private String emailPersonal;

    @Size(max = 50, message = "El estado civil no puede exceder los 50 caracteres.")
    private String estadoCivil;

    @Size(max = 50, message = "El tipo de documento no puede exceder los 50 caracteres.")
    private String tipoDocumento;


    @Size(max = 100, message = "La ocupación no puede exceder los 100 caracteres.")
    private String ocupacion;

    @Size(max = 255, message = "El lugar de trabajo no puede exceder los 255 caracteres.")
    private String lugarTrabajo;

    @Size(max = 9, message = "El teléfono de trabajo no puede exceder los 9 caracteres.")
    private String telefonoTrabajo;

    @NotBlank(message = "El parentesco no puede estar vacío.")
    @Size(max = 50, message = "El parentesco no puede exceder los 50 caracteres.")
    private String parentesco;

    @Size(max = 50, message = "El nivel educativo no puede exceder los 50 caracteres.")
    private String nivelEducativo;


    @DecimalMin(value = "0.0", inclusive = true, message = "El ingreso mensual no puede ser negativo.")

    private BigDecimal ingresoMensual;

    @NotNull(message = "Debe especificar si es apoderado principal.")
    private Boolean esPrincipal;

    @NotNull(message = "Debe especificar si está autorizado a recoger.")
    private Boolean autorizadoRecoger;

    @Size(max = 255, message = "La referencia personal no puede exceder los 255 caracteres.")
    private String referenciaPersonal;

    @Size(max = 20, message = "El teléfono de referencia no puede exceder los 20 caracteres.")
    private String telefonoReferencia;


    private Integer idUsuario;

    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    @Size(min = 4, max = 50, message = "El nombre de usuario debe tener entre 4 y 50 caracteres.")
    private String userName;


    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    private String password;

    private String rutaImagen;

    private MultipartFile foto;

    private String confirmPassword;

    public boolean isPasswordRequired() {
        return this.idApoderado == null;

    }
}