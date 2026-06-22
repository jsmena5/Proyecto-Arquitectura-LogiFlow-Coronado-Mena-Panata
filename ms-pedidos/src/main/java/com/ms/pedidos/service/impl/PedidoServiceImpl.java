package com.ms.pedidos.service.impl;

import com.ms.pedidos.config.RabbitMQConfig;
import com.ms.pedidos.dto.EstadoPedidoRequestDTO;
import com.ms.pedidos.dto.PedidoRequestDTO;
import com.ms.pedidos.dto.PedidoResponseDTO;
import com.ms.pedidos.entity.Pedido;
import com.ms.pedidos.enums.EstadoPedido;
import com.ms.pedidos.event.PedidoCanceladoEvent;
import com.ms.pedidos.event.PedidoCreadoEvent;
import com.ms.pedidos.repository.PedidoRepository;
import com.ms.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public PedidoResponseDTO crearPedido(PedidoRequestDTO requestDTO) {

        Pedido pedido = Pedido.builder()
                .clienteId(requestDTO.getClienteId())
                .direccionOrigen(requestDTO.getDireccionOrigen())
                .direccionDestino(requestDTO.getDireccionDestino())
                .descripcion(requestDTO.getDescripcion())
                .estado(EstadoPedido.CREADO)
                .build();

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        PedidoCreadoEvent event = PedidoCreadoEvent.builder()
                .pedidoId(pedidoGuardado.getId())
                .clienteId(pedidoGuardado.getClienteId())
                .direccionOrigen(pedidoGuardado.getDireccionOrigen())
                .direccionDestino(pedidoGuardado.getDireccionDestino())
                .descripcion(pedidoGuardado.getDescripcion())
                .fechaCreacion(pedidoGuardado.getFechaCreacion())
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.LOGIFLOW_EXCHANGE,
                RabbitMQConfig.PEDIDO_CREADO_ROUTING_KEY,
                event
        );

        return mapToResponseDTO(pedidoGuardado);
    }

    @Override
    public List<PedidoResponseDTO> listarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public PedidoResponseDTO obtenerPedidoPorId(UUID id) {
        Pedido pedido = buscarPedidoPorId(id);
        return mapToResponseDTO(pedido);
    }

    @Override
    public PedidoResponseDTO actualizarEstado(UUID id, EstadoPedidoRequestDTO requestDTO) {

        Pedido pedido = buscarPedidoPorId(id);

        pedido.setEstado(requestDTO.getEstado());

        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        if (requestDTO.getEstado() == EstadoPedido.CANCELADO) {
            publicarEventoPedidoCancelado(pedidoActualizado, "Pedido cancelado por actualización de estado");
        }

        return mapToResponseDTO(pedidoActualizado);
    }

    @Override
    public void cancelarPedido(UUID id) {

        Pedido pedido = buscarPedidoPorId(id);

        if (pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("El pedido ya se encuentra cancelado");
        }

        pedido.setEstado(EstadoPedido.CANCELADO);

        Pedido pedidoCancelado = pedidoRepository.save(pedido);

        publicarEventoPedidoCancelado(pedidoCancelado, "Pedido cancelado desde el endpoint DELETE");
    }

    private Pedido buscarPedidoPorId(UUID id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el pedido con ID: " + id));
    }

    private void publicarEventoPedidoCancelado(Pedido pedido, String motivo) {

        PedidoCanceladoEvent event = PedidoCanceladoEvent.builder()
                .pedidoId(pedido.getId())
                .clienteId(pedido.getClienteId())
                .motivo(motivo)
                .fechaCancelacion(LocalDateTime.now())
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.LOGIFLOW_EXCHANGE,
                RabbitMQConfig.PEDIDO_CANCELADO_ROUTING_KEY,
                event
        );
    }

    private PedidoResponseDTO mapToResponseDTO(Pedido pedido) {
        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .clienteId(pedido.getClienteId())
                .direccionOrigen(pedido.getDireccionOrigen())
                .direccionDestino(pedido.getDireccionDestino())
                .descripcion(pedido.getDescripcion())
                .estado(pedido.getEstado())
                .fechaCreacion(pedido.getFechaCreacion())
                .fechaActualizacion(pedido.getFechaActualizacion())
                .build();
    }
}