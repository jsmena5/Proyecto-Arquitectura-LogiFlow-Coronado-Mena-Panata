package com.ms.ruteo.event;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoCreadoEvent {

    private UUID pedidoId;
    private UUID clienteId;
    private String direccionOrigen;
    private String direccionDestino;
    private String descripcion;
    private LocalDateTime fechaCreacion;
}