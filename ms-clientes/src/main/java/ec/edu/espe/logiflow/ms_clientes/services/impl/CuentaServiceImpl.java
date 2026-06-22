package ec.edu.espe.logiflow.ms_clientes.services.impl;

import ec.edu.espe.logiflow.ms_clientes.dto.request.CuentaCreateRequest;
import ec.edu.espe.logiflow.ms_clientes.dto.response.CuentaResponse;
import ec.edu.espe.logiflow.ms_clientes.models.Cliente;
import ec.edu.espe.logiflow.ms_clientes.models.Cuenta;
import ec.edu.espe.logiflow.ms_clientes.repositories.ClienteRepository;
import ec.edu.espe.logiflow.ms_clientes.repositories.CuentaRepository;
import ec.edu.espe.logiflow.ms_clientes.services.CuentaService;
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
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public CuentaResponse createCuenta(UUID clienteId, CuentaCreateRequest request) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado para asociar la cuenta"));

        if (!cliente.getEsCorporativo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede crear una cuenta de crédito a un cliente que no es corporativo");
        }

        if (cuentaRepository.existsByNumeroContrato(request.getNumeroContrato().toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El número de contrato ya está registrado");
        }

        Cuenta cuenta = Cuenta.builder()
                .cliente(cliente)
                .numeroContrato(request.getNumeroContrato().toUpperCase())
                .limiteCredito(request.getLimiteCredito())
                .saldoUtilizado(0.0) // Inicia en 0
                .activa(true)
                .build();

        return mapToResponse(cuentaRepository.save(cuenta));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaResponse> getCuentasByClienteId(UUID clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }

        return cuentaRepository.findByClienteId(clienteId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteCuenta(UUID cuentaId) {
        if (!cuentaRepository.existsById(cuentaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada con el ID proporcionado");
        }
        cuentaRepository.deleteById(cuentaId);
    }

    private CuentaResponse mapToResponse(Cuenta cuenta) {
        return CuentaResponse.builder()
                .id(cuenta.getId())
                .clienteId(cuenta.getCliente().getId())
                .numeroContrato(cuenta.getNumeroContrato())
                .limiteCredito(cuenta.getLimiteCredito())
                .saldoUtilizado(cuenta.getSaldoUtilizado())
                .activa(cuenta.getActiva())
                .createdAt(cuenta.getCreatedAt())
                .build();
    }
}