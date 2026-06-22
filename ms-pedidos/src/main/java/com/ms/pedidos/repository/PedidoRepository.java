package com.ms.pedidos.repository;

import com.ms.pedidos.entity.Pedido;
import com.ms.pedidos.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    List<Pedido> findByClienteId(UUID clienteId);

    List<Pedido> findByEstado(EstadoPedido estado);
}