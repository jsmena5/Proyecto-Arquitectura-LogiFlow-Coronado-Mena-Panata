package ec.edu.espe.logiflow.ms_clientes.services;

import ec.edu.espe.logiflow.ms_clientes.dto.request.ClienteCreateRequest;
import ec.edu.espe.logiflow.ms_clientes.dto.request.ClienteUpdateRequest;
import ec.edu.espe.logiflow.ms_clientes.dto.response.ClienteResponse;

import java.util.List;
import java.util.UUID;

public interface ClienteService {
    ClienteResponse createCliente(ClienteCreateRequest request);
    ClienteResponse updateCliente(UUID id, ClienteUpdateRequest request);
    ClienteResponse getClienteById(UUID id);
    List<ClienteResponse> getAllClientes();
    void deleteCliente(UUID id);
}