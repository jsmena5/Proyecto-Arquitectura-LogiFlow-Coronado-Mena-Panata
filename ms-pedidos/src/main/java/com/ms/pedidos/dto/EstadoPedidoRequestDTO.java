package com.ms.pedidos.dto;

import com.ms.pedidos.enums.EstadoPedido;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoPedidoRequestDTO {

    @NotNull(message = "El estado del pedido es obligatorio")
    private EstadoPedido estado;
}