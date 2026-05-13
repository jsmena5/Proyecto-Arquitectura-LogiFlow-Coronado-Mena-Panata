package ec.edu.espe.logiflow.flota.controller;

import ec.edu.espe.logiflow.flota.dto.request.ConductorCreateRequest;
import ec.edu.espe.logiflow.flota.dto.request.ConductorUpdateRequest;
import ec.edu.espe.logiflow.flota.dto.response.ConductorResponse;
import ec.edu.espe.logiflow.flota.services.ConductorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/conductores")
@RequiredArgsConstructor
@Tag(name = "Conductores", description = "CRUD de conductores")
public class ConductorController {

    private final ConductorService conductorService;

    @PostMapping
    public ResponseEntity<ConductorResponse> createConductor(@Valid @RequestBody ConductorCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(conductorService.createConductor(request));
    }

    @GetMapping
    public ResponseEntity<List<ConductorResponse>> getAllConductores() {
        return ResponseEntity.ok(conductorService.getAllConductores());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConductorResponse> updateConductor(
            @PathVariable UUID id,
            @Valid @RequestBody ConductorUpdateRequest request) {
        return ResponseEntity.ok(conductorService.updateConductor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConductor(@PathVariable UUID id) {
        conductorService.deleteConductor(id);
        return ResponseEntity.noContent().build();
    }
}
