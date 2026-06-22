package com.ms.pedidos.controller;

import com.ms.pedidos.dto.EstadoPedidoRequestDTO;
import com.ms.pedidos.dto.PedidoRequestDTO;
import com.ms.pedidos.dto.PedidoResponseDTO;
import com.ms.pedidos.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Crear pedido", description = "Crea un pedido y publica el evento PedidoCreado en RabbitMQ")
    public ResponseEntity<PedidoResponseDTO> crearPedido(@Valid @RequestBody PedidoRequestDTO requestDTO) {
        PedidoResponseDTO response = pedidoService.crearPedido(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar pedidos", description = "Obtiene todos los pedidos registrados")
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID", description = "Obtiene un pedido específico mediante su ID")
    public ResponseEntity<PedidoResponseDTO> obtenerPedidoPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(id));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado", description = "Actualiza el estado de un pedido")
    public ResponseEntity<PedidoResponseDTO> actualizarEstado(
            @PathVariable UUID id,
            @Valid @RequestBody EstadoPedidoRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido", description = "Cancela un pedido y publica el evento PedidoCancelado")
    public ResponseEntity<Void> cancelarPedido(@PathVariable UUID id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}