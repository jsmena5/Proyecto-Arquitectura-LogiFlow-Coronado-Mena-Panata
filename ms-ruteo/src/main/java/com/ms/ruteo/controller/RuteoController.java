package com.ms.ruteo.controller;

import com.ms.ruteo.dto.AsignarRutaRequestDTO;
import com.ms.ruteo.dto.EnvioResponseDTO;
import com.ms.ruteo.service.RuteoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ruteo")
@RequiredArgsConstructor
public class RuteoController {

    private final RuteoService ruteoService;

    @PostMapping("/asignar")
    @Operation(summary = "Asignar ruta", description = "Asigna una ruta óptima simple a un pedido")
    public ResponseEntity<EnvioResponseDTO> asignarRuta(@Valid @RequestBody AsignarRutaRequestDTO requestDTO) {
        EnvioResponseDTO response = ruteoService.asignarRuta(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/envios")
    @Operation(summary = "Listar envíos", description = "Obtiene todos los envíos asignados")
    public ResponseEntity<List<EnvioResponseDTO>> listarEnvios() {
        return ResponseEntity.ok(ruteoService.listarEnvios());
    }

    @GetMapping("/envios/{id}")
    @Operation(summary = "Obtener envío por ID", description = "Obtiene un envío específico mediante su ID")
    public ResponseEntity<EnvioResponseDTO> obtenerEnvioPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(ruteoService.obtenerEnvioPorId(id));
    }
}