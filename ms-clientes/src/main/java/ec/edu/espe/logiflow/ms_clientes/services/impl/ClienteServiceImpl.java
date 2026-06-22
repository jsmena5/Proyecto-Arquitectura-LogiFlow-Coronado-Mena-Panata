package ec.edu.espe.logiflow.ms_clientes.services.impl;

import ec.edu.espe.logiflow.ms_clientes.dto.request.ClienteCreateRequest;
import ec.edu.espe.logiflow.ms_clientes.dto.request.ClienteUpdateRequest;
import ec.edu.espe.logiflow.ms_clientes.dto.response.ClienteResponse;
import ec.edu.espe.logiflow.ms_clientes.models.Cliente;
import ec.edu.espe.logiflow.ms_clientes.repositories.ClienteRepository;
import ec.edu.espe.logiflow.ms_clientes.services.ClienteService;
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
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public ClienteResponse createCliente(ClienteCreateRequest request) {
        if (clienteRepository.existsByIdentificacion(request.getIdentificacion())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La identificación (DNI/RUC) ya está registrada.");
        }
        if (clienteRepository.existsByCorreo(request.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo electrónico ya está en uso.");
        }

        Cliente cliente = Cliente.builder()
                .identificacion(request.getIdentificacion())
                .razonSocial(request.getRazonSocial().trim())
                .correo(request.getCorreo().toLowerCase().trim())
                .telefono(request.getTelefono())
                .esCorporativo(request.getEsCorporativo())
                .activo(true)
                .build();

        return mapToResponse(clienteRepository.save(cliente));
    }

    @Override
    public ClienteResponse updateCliente(UUID id, ClienteUpdateRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        // Verificamos si el correo cambió y si el nuevo correo ya existe
        if (!cliente.getCorreo().equalsIgnoreCase(request.getCorreo()) && clienteRepository.existsByCorreo(request.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nuevo correo electrónico ya está en uso por otro cliente.");
        }

        cliente.setRazonSocial(request.getRazonSocial().trim());
        cliente.setCorreo(request.getCorreo().toLowerCase().trim());
        cliente.setTelefono(request.getTelefono());
        cliente.setActivo(request.getActivo());

        return mapToResponse(clienteRepository.save(cliente));
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse getClienteById(UUID id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        return mapToResponse(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> getAllClientes() {
        return clienteRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteCliente(UUID id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con el ID proporcionado");
        }
        clienteRepository.deleteById(id);
    }

    private ClienteResponse mapToResponse(Cliente cliente) {
        return ClienteResponse.builder()
                .id(cliente.getId())
                .identificacion(cliente.getIdentificacion())
                .razonSocial(cliente.getRazonSocial())
                .correo(cliente.getCorreo())
                .telefono(cliente.getTelefono())
                .esCorporativo(cliente.getEsCorporativo())
                .activo(cliente.getActivo())
                .createdAt(cliente.getCreatedAt())
                .build();
    }
}