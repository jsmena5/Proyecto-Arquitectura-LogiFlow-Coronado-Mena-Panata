package ec.edu.espe.logiflow.flota.services.impl;

import ec.edu.espe.logiflow.flota.dto.request.VehiculoCreateRequest;
import ec.edu.espe.logiflow.flota.dto.request.VehiculoUpdateRequest;
import ec.edu.espe.logiflow.flota.dto.response.VehiculoResponse;
import ec.edu.espe.logiflow.flota.enums.EstadoVehiculo;
import ec.edu.espe.logiflow.flota.models.Vehiculo;
import ec.edu.espe.logiflow.flota.repositories.VehiculoRepository;
import ec.edu.espe.logiflow.flota.services.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    @Override
    public VehiculoResponse createVehiculo(VehiculoCreateRequest request) {
        String matricula = request.getMatricula().toUpperCase();

        if (vehiculoRepository.existsByMatricula(matricula)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Matrícula ya registrada");
        }

        Vehiculo vehiculo = Vehiculo.builder()
                .matricula(matricula)
                .tipo(request.getTipo())
                .capacidadKg(request.getCapacidadKg())
                .autonomiaKm(request.getAutonomiaKm())
                .estado(EstadoVehiculo.DISPONIBLE)
                .build();

        return mapToResponse(vehiculoRepository.save(vehiculo));
    }

    @Override
    public VehiculoResponse updateVehiculo(UUID id, VehiculoUpdateRequest request) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado"));

        vehiculo.setEstado(request.getEstado());
        vehiculo.setCapacidadKg(request.getCapacidadKg());
        vehiculo.setAutonomiaKm(request.getAutonomiaKm());

        return mapToResponse(vehiculoRepository.save(vehiculo));
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculoResponse getVehiculoByMatricula(String matricula) {
        Vehiculo vehiculo = vehiculoRepository.findByMatricula(matricula.toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado"));
        return mapToResponse(vehiculo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoResponse> getAllVehiculos() {
        return vehiculoRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoResponse> getVehiculosDisponibles() {
        return vehiculoRepository.findByEstado(EstadoVehiculo.DISPONIBLE).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteVehiculo(UUID id) {
        if (!vehiculoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado con el ID proporcionado");
        }
        vehiculoRepository.deleteById(id);
    }

    private VehiculoResponse mapToResponse(Vehiculo vehiculo) {
        return VehiculoResponse.builder()
                .id(vehiculo.getId())
                .matricula(vehiculo.getMatricula())
                .tipo(vehiculo.getTipo())
                .capacidadKg(vehiculo.getCapacidadKg())
                .autonomiaKm(vehiculo.getAutonomiaKm())
                .estado(vehiculo.getEstado())
                .createdAt(vehiculo.getCreatedAt())
                .build();
    }
}
