package com.ms.ruteo.event;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculoDisponibleEvent {

    private UUID vehiculoId;
    private String placa;
    private String tipoVehiculo;
    private String ubicacionActual;
    private LocalDateTime fechaDisponibilidad;
}