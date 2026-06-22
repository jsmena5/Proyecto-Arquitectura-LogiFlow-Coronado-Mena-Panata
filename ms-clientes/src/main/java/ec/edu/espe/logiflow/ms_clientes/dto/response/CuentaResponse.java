package ec.edu.espe.logiflow.ms_clientes.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CuentaResponse {
    private UUID id;
    private UUID clienteId;
    private String numeroContrato;
    private Double limiteCredito;
    private Double saldoUtilizado;
    private Boolean activa;
    private LocalDateTime createdAt;
}