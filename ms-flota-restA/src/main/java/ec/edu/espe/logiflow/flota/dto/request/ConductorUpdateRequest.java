package ec.edu.espe.logiflow.flota.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConductorUpdateRequest {

    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(min = 2, max = 25, message = "El primer nombre debe tener entre 2 y 25 caracteres")
    @Pattern(
            regexp = "^(?!.*([a-zA-ZáéíóúÁÉÍÓÚñÑ])\\1{2})[A-ZÁÉÍÓÚÑ][a-záéíóúñ]+$",
            message = "El primer nombre debe comenzar con mayúscula, seguido de minúsculas, sin números ni caracteres especiales, y no debe contener la misma letra repetida 3 veces seguidas."
    )
    private String primerNombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 25, message = "El apellido debe tener entre 2 y 25 caracteres")
    @Pattern(
            regexp = "^(?!.*([a-zA-ZáéíóúÁÉÍÓÚñÑ])\\1{2})[A-ZÁÉÍÓÚÑ][a-záéíóúñ]+$",
            message = "El apellido debe comenzar con mayúscula, seguido de minúsculas, sin números ni caracteres especiales, y no debe contener la misma letra repetida 3 veces seguidas."
    )
    private String apellido;

    @NotBlank(message = "El tipo de licencia es obligatorio")
    @Pattern(regexp = "^[ABCE]$", message = "Tipo de licencia no válido. Valores permitidos: A, B, C o E")
    private String tipoLicencia;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
}