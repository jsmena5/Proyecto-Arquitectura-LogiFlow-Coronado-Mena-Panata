package com.ms.ruteo.dto;

import com.ms.ruteo.enums.EstadoEnvio;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioResponseDTO {

    private UUID id;
    private UUID pedidoId;
    private UUID clienteId;
    private UUID vehiculoId;
    private String direccionOrigen;
    private String direccionDestino;
    private String rutaAsignada;
    private EstadoEnvio estado;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaActualizacion;
}