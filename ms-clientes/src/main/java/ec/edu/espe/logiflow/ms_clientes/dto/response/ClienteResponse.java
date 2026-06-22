package ec.edu.espe.logiflow.ms_clientes.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ClienteResponse {
    private UUID id;
    private String identificacion;
    private String razonSocial;
    private String correo;
    private String telefono;
    private Boolean esCorporativo;
    private Boolean activo;
    private LocalDateTime createdAt;
}