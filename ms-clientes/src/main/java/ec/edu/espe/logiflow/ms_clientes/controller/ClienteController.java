package ec.edu.espe.logiflow.ms_clientes.controller;

import ec.edu.espe.logiflow.ms_clientes.dto.request.ClienteCreateRequest;
import ec.edu.espe.logiflow.ms_clientes.dto.request.ClienteUpdateRequest;
import ec.edu.espe.logiflow.ms_clientes.dto.request.CuentaCreateRequest;
import ec.edu.espe.logiflow.ms_clientes.dto.response.ClienteResponse;
import ec.edu.espe.logiflow.ms_clientes.dto.response.CuentaResponse;
import ec.edu.espe.logiflow.ms_clientes.services.ClienteService;
import ec.edu.espe.logiflow.ms_clientes.services.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operaciones CRUD de clientes y sus cuentas corporativas")
public class ClienteController {

    private final ClienteService clienteService;
    private final CuentaService cuentaService;

    // ==========================================
    // ENDPOINTS DE CLIENTES
    // ==========================================

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente")
    public ResponseEntity<ClienteResponse> createCliente(@Valid @RequestBody ClienteCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.createCliente(request));
    }

    @GetMapping
    @Operation(summary = "Listar todos los clientes")
    public ResponseEntity<List<ClienteResponse>> getAllClientes() {
        return ResponseEntity.ok(clienteService.getAllClientes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un cliente específico por su ID")
    public ResponseEntity<ClienteResponse> getClienteById(@PathVariable UUID id) {
        return ResponseEntity.ok(clienteService.getClienteById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar la información de un cliente")
    public ResponseEntity<ClienteResponse> updateCliente(
            @PathVariable UUID id,
            @Valid @RequestBody ClienteUpdateRequest request) {
        return ResponseEntity.ok(clienteService.updateCliente(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente")
    public ResponseEntity<Void> deleteCliente(@PathVariable UUID id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // ENDPOINTS DE CUENTAS ASOCIADAS
    // ==========================================

    @PostMapping("/{id}/cuentas")
    @Operation(summary = "Crear una nueva cuenta corporativa para un cliente")
    public ResponseEntity<CuentaResponse> createCuenta(
            @PathVariable UUID id,
            @Valid @RequestBody CuentaCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.createCuenta(id, request));
    }

    @GetMapping("/{id}/cuentas")
    @Operation(summary = "Obtener todas las cuentas corporativas de un cliente")
    public ResponseEntity<List<CuentaResponse>> getCuentasByClienteId(@PathVariable UUID id) {
        return ResponseEntity.ok(cuentaService.getCuentasByClienteId(id));
    }

    @DeleteMapping("/{id}/cuentas/{cuentaId}")
    @Operation(summary = "Eliminar una cuenta corporativa asociada a un cliente")
    public ResponseEntity<Void> deleteCuenta(
            @PathVariable UUID id,
            @PathVariable UUID cuentaId) {
        cuentaService.deleteCuenta(cuentaId);
        return ResponseEntity.noContent().build();
    }
}