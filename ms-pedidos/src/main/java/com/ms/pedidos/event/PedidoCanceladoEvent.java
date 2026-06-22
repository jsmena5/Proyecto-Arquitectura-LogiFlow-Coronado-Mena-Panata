package com.ms.pedidos.event;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoCanceladoEvent {

    private UUID pedidoId;
    private UUID clienteId;
    private String motivo;
    private LocalDateTime fechaCancelacion;
}