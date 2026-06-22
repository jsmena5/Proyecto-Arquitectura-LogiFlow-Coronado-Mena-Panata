package com.ms.pedidos.service;

import com.ms.pedidos.dto.EstadoPedidoRequestDTO;
import com.ms.pedidos.dto.PedidoRequestDTO;
import com.ms.pedidos.dto.PedidoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PedidoService {

    PedidoResponseDTO crearPedido(PedidoRequestDTO requestDTO);

    List<PedidoResponseDTO> listarPedidos();

    PedidoResponseDTO obtenerPedidoPorId(UUID id);

    PedidoResponseDTO actualizarEstado(UUID id, EstadoPedidoRequestDTO requestDTO);

    void cancelarPedido(UUID id);
}