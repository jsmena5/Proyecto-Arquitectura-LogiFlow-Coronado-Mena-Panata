package ec.edu.espe.logiflow.flota.controller;

import ec.edu.espe.logiflow.flota.dto.request.VehiculoCreateRequest;
import ec.edu.espe.logiflow.flota.dto.request.VehiculoUpdateRequest;
import ec.edu.espe.logiflow.flota.dto.response.VehiculoResponse;
import ec.edu.espe.logiflow.flota.services.VehiculoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehiculos")
@RequiredArgsConstructor
@Tag(name = "Vehículos", description = "CRUD de vehículos y consulta de disponibilidad")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @PostMapping
    public ResponseEntity<VehiculoResponse> createVehiculo(@Valid @RequestBody VehiculoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculoService.createVehiculo(request));
    }

    @GetMapping
    public ResponseEntity<List<VehiculoResponse>> getAllVehiculos() {
        return ResponseEntity.ok(vehiculoService.getAllVehiculos());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<VehiculoResponse>> getVehiculosDisponibles() {
        return ResponseEntity.ok(vehiculoService.getVehiculosDisponibles());
    }

    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<VehiculoResponse> getVehiculoByMatricula(@PathVariable String matricula) {
        return ResponseEntity.ok(vehiculoService.getVehiculoByMatricula(matricula));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculoResponse> updateVehiculo(
            @PathVariable UUID id,
            @Valid @RequestBody VehiculoUpdateRequest request) {
        return ResponseEntity.ok(vehiculoService.updateVehiculo(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehiculo(@PathVariable UUID id) {
        vehiculoService.deleteVehiculo(id);
        return ResponseEntity.noContent().build();
    }
}
