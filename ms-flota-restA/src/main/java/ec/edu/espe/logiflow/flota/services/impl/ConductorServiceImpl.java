package ec.edu.espe.logiflow.flota.services.impl;

import ec.edu.espe.logiflow.flota.dto.request.ConductorCreateRequest;
import ec.edu.espe.logiflow.flota.dto.request.ConductorUpdateRequest;
import ec.edu.espe.logiflow.flota.dto.response.ConductorResponse;
import ec.edu.espe.logiflow.flota.models.Conductor;
import ec.edu.espe.logiflow.flota.repositories.ConductorRepository;
import ec.edu.espe.logiflow.flota.services.ConductorService;
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
public class ConductorServiceImpl implements ConductorService {

    private final ConductorRepository conductorRepository;

    @Override
    public ConductorResponse createConductor(ConductorCreateRequest request) {
        if (conductorRepository.existsByDni(request.getDni())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El DNI ya está registrado");
        }
        if (conductorRepository.existsByNumeroLicencia(request.getNumeroLicencia().toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La licencia ya está registrada");
        }

        Conductor conductor = Conductor.builder()
                .dni(request.getDni())
                .primerNombre(request.getPrimerNombre().trim())
                .apellido(request.getApellido().trim())
                .numeroLicencia(request.getNumeroLicencia().toUpperCase())
                .tipoLicencia(request.getTipoLicencia().toUpperCase())
                .activo(true)
                .build();

        return mapToResponse(conductorRepository.save(conductor));
    }

    @Override
    public ConductorResponse updateConductor(UUID id, ConductorUpdateRequest request) {
        Conductor conductor = conductorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conductor no encontrado"));

        conductor.setPrimerNombre(request.getPrimerNombre().trim());
        conductor.setApellido(request.getApellido().trim());
        conductor.setTipoLicencia(request.getTipoLicencia().toUpperCase());
        conductor.setActivo(request.getActivo());

        return mapToResponse(conductorRepository.save(conductor));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConductorResponse> getAllConductores() {
        return conductorRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteConductor(UUID id) {
        if (!conductorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conductor no encontrado con el ID proporcionado");
        }
        conductorRepository.deleteById(id);
    }

    private ConductorResponse mapToResponse(Conductor conductor) {
        return ConductorResponse.builder()
                .id(conductor.getId())
                .dni(conductor.getDni())
                .primerNombre(conductor.getPrimerNombre())
                .apellido(conductor.getApellido())
                .numeroLicencia(conductor.getNumeroLicencia())
                .tipoLicencia(conductor.getTipoLicencia())
                .activo(conductor.getActivo())
                .createdAt(conductor.getCreatedAt())
                .build();
    }
}