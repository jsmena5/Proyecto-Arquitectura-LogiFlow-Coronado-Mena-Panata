package com.ms.pedidos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoRequestDTO {

    @NotNull(message = "El clienteId es obligatorio")
    private UUID clienteId;

    @NotBlank(message = "La dirección de origen es obligatoria")
    @Size(max = 200, message = "La dirección de origen no puede superar los 200 caracteres")
    private String direccionOrigen;

    @NotBlank(message = "La dirección de destino es obligatoria")
    @Size(max = 200, message = "La dirección de destino no puede superar los 200 caracteres")
    private String direccionDestino;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;
}