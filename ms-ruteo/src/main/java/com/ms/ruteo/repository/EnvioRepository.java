package com.ms.ruteo.repository;

import com.ms.ruteo.entity.Envio;
import com.ms.ruteo.enums.EstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnvioRepository extends JpaRepository<Envio, UUID> {

    Optional<Envio> findByPedidoId(UUID pedidoId);

    List<Envio> findByClienteId(UUID clienteId);

    List<Envio> findByEstado(EstadoEnvio estado);
}