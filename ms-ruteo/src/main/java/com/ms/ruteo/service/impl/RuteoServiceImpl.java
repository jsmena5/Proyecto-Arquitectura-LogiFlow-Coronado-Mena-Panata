package com.ms.ruteo.service.impl;

import com.ms.ruteo.config.RabbitMQConfig;
import com.ms.ruteo.dto.AsignarRutaRequestDTO;
import com.ms.ruteo.dto.EnvioResponseDTO;
import com.ms.ruteo.dto.RutaCalculadaDTO;
import com.ms.ruteo.entity.Envio;
import com.ms.ruteo.enums.EstadoEnvio;
import com.ms.ruteo.event.EnvioAsignadoEvent;
import com.ms.ruteo.event.PedidoCreadoEvent;
import com.ms.ruteo.repository.EnvioRepository;
import com.ms.ruteo.service.RuteoService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RuteoServiceImpl implements RuteoService {

    private final EnvioRepository envioRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public EnvioResponseDTO asignarRuta(AsignarRutaRequestDTO requestDTO) {

        envioRepository.findByPedidoId(requestDTO.getPedidoId())
                .ifPresent(envio -> {
                    throw new RuntimeException("Ya existe un envío asignado para el pedido ID: " + requestDTO.getPedidoId());
                });

        RutaCalculadaDTO rutaCalculada = calcularRutaSimple(
                requestDTO.getDireccionOrigen(),
                requestDTO.getDireccionDestino()
        );

        Envio envio = Envio.builder()
                .pedidoId(requestDTO.getPedidoId())
                .clienteId(requestDTO.getClienteId())
                .vehiculoId(requestDTO.getVehiculoId())
                .direccionOrigen(requestDTO.getDireccionOrigen())
                .direccionDestino(requestDTO.getDireccionDestino())
                .rutaAsignada(rutaCalculada.getRuta())
                .estado(EstadoEnvio.ASIGNADO)
                .build();

        Envio envioGuardado = envioRepository.save(envio);

        publicarEventoEnvioAsignado(envioGuardado);

        return mapToResponseDTO(envioGuardado);
    }

    @Override
    public EnvioResponseDTO asignarRutaDesdePedidoCreado(PedidoCreadoEvent event) {

        envioRepository.findByPedidoId(event.getPedidoId())
                .ifPresent(envio -> {
                    throw new RuntimeException("El pedido ya tiene un envío asignado: " + event.getPedidoId());
                });

        RutaCalculadaDTO rutaCalculada = calcularRutaSimple(
                event.getDireccionOrigen(),
                event.getDireccionDestino()
        );

        Envio envio = Envio.builder()
                .pedidoId(event.getPedidoId())
                .clienteId(event.getClienteId())
                .vehiculoId(null)
                .direccionOrigen(event.getDireccionOrigen())
                .direccionDestino(event.getDireccionDestino())
                .rutaAsignada(rutaCalculada.getRuta())
                .estado(EstadoEnvio.ASIGNADO)
                .build();

        Envio envioGuardado = envioRepository.save(envio);

        publicarEventoEnvioAsignado(envioGuardado);

        return mapToResponseDTO(envioGuardado);
    }

    @Override
    public List<EnvioResponseDTO> listarEnvios() {
        return envioRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public EnvioResponseDTO obtenerEnvioPorId(UUID id) {
        Envio envio = buscarEnvioPorId(id);
        return mapToResponseDTO(envio);
    }

    private RutaCalculadaDTO calcularRutaSimple(String origen, String destino) {

        String ruta = "Ruta óptima simple: " + origen + " -> Av. Principal -> Vía más corta disponible -> " + destino;

        return RutaCalculadaDTO.builder()
                .origen(origen)
                .destino(destino)
                .ruta(ruta)
                .distanciaEstimadaKm(12.5)
                .tiempoEstimadoMinutos(35)
                .build();
    }

    private void publicarEventoEnvioAsignado(Envio envio) {

        EnvioAsignadoEvent event = EnvioAsignadoEvent.builder()
                .envioId(envio.getId())
                .pedidoId(envio.getPedidoId())
                .clienteId(envio.getClienteId())
                .vehiculoId(envio.getVehiculoId())
                .rutaAsignada(envio.getRutaAsignada())
                .fechaAsignacion(LocalDateTime.now())
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.LOGIFLOW_EXCHANGE,
                RabbitMQConfig.ENVIO_ASIGNADO_ROUTING_KEY,
                event
        );
    }

    private Envio buscarEnvioPorId(UUID id) {
        return envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el envío con ID: " + id));
    }

    private EnvioResponseDTO mapToResponseDTO(Envio envio) {
        return EnvioResponseDTO.builder()
                .id(envio.getId())
                .pedidoId(envio.getPedidoId())
                .clienteId(envio.getClienteId())
                .vehiculoId(envio.getVehiculoId())
                .direccionOrigen(envio.getDireccionOrigen())
                .direccionDestino(envio.getDireccionDestino())
                .rutaAsignada(envio.getRutaAsignada())
                .estado(envio.getEstado())
                .fechaAsignacion(envio.getFechaAsignacion())
                .fechaActualizacion(envio.getFechaActualizacion())
                .build();
    }
}