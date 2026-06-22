package com.ms.ruteo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignarRutaRequestDTO {

    @NotNull(message = "El pedidoId es obligatorio")
    private UUID pedidoId;

    @NotNull(message = "El clienteId es obligatorio")
    private UUID clienteId;

    private UUID vehiculoId;

    @NotBlank(message = "La dirección de origen es obligatoria")
    private String direccionOrigen;

    @NotBlank(message = "La dirección de destino es obligatoria")
    private String direccionDestino;
}