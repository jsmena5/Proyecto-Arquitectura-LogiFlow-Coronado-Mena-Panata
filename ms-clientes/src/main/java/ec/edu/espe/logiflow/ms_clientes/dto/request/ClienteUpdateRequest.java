package ec.edu.espe.logiflow.ms_clientes.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClienteUpdateRequest {

    @NotBlank(message = "La razón social o nombre es obligatorio")
    @Size(min = 3, max = 100, message = "La razón social debe tener entre 3 y 100 caracteres")
    @Pattern(
            regexp = "^(?!.*([a-zA-ZáéíóúÁÉÍÓÚñÑ0-9])\\1{2})[A-ZÁÉÍÓÚÑ0-9][a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s.,&\\-]+$",
            message = "La razón social debe iniciar con mayúscula o número, no contener caracteres especiales no permitidos, y no tener la misma letra/número repetida 3 veces seguidas."
    )
    private String razonSocial;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^09[0-9]{8}$|^0[2-8][0-9]{7}$", message = "El teléfono debe ser un celular válido de Ecuador (ej. 09...) o un teléfono fijo con código de provincia.")
    private String telefono;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
}