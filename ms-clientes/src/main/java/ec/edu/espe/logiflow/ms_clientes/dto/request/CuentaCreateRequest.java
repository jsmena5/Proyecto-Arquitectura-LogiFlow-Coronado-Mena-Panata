package ec.edu.espe.logiflow.ms_clientes.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CuentaCreateRequest {

    @NotBlank(message = "El número de contrato es obligatorio")
    @Pattern(regexp = "^CTR-[0-9]{5,8}$", message = "El contrato debe tener el formato CTR-##### (ej. CTR-12345)")
    private String numeroContrato;

    @NotNull(message = "El límite de crédito es obligatorio")
    @DecimalMin(value = "0.0", message = "El límite de crédito no puede ser negativo")
    @DecimalMax(value = "500000.0", message = "El límite de crédito máximo permitido es 500,000.00")
    @Digits(integer = 6, fraction = 2, message = "El límite de crédito debe tener máximo 6 enteros y 2 decimales")
    private Double limiteCredito;
}