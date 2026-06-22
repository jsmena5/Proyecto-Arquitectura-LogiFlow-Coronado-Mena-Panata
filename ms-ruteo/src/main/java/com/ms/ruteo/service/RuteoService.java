package com.ms.ruteo.service;

import com.ms.ruteo.dto.AsignarRutaRequestDTO;
import com.ms.ruteo.dto.EnvioResponseDTO;
import com.ms.ruteo.event.PedidoCreadoEvent;

import java.util.List;
import java.util.UUID;

public interface RuteoService {

    EnvioResponseDTO asignarRuta(AsignarRutaRequestDTO requestDTO);

    EnvioResponseDTO asignarRutaDesdePedidoCreado(PedidoCreadoEvent event);

    List<EnvioResponseDTO> listarEnvios();

    EnvioResponseDTO obtenerEnvioPorId(UUID id);
}