package com.ms.ruteo.event;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioAsignadoEvent {

    private UUID envioId;
    private UUID pedidoId;
    private UUID clienteId;
    private UUID vehiculoId;
    private String rutaAsignada;
    private LocalDateTime fechaAsignacion;
}