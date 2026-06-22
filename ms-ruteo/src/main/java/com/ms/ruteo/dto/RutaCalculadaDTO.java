package com.ms.ruteo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RutaCalculadaDTO {

    private String origen;
    private String destino;
    private String ruta;
    private Double distanciaEstimadaKm;
    private Integer tiempoEstimadoMinutos;
}