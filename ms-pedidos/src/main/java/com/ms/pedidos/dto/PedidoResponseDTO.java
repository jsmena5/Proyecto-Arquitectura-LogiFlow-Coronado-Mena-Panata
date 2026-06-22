package com.ms.pedidos.dto;

import com.ms.pedidos.enums.EstadoPedido;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponseDTO {

    private UUID id;
    private UUID clienteId;
    private String direccionOrigen;
    private String direccionDestino;
    private String descripcion;
    private EstadoPedido estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}