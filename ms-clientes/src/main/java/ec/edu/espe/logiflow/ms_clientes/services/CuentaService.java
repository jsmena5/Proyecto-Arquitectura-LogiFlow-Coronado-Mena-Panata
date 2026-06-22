package ec.edu.espe.logiflow.ms_clientes.services;

import ec.edu.espe.logiflow.ms_clientes.dto.request.CuentaCreateRequest;
import ec.edu.espe.logiflow.ms_clientes.dto.response.CuentaResponse;

import java.util.List;
import java.util.UUID;

public interface CuentaService {
    CuentaResponse createCuenta(UUID clienteId, CuentaCreateRequest request);
    List<CuentaResponse> getCuentasByClienteId(UUID clienteId);
    void deleteCuenta(UUID cuentaId);
}