package ec.edu.espe.logiflow.ms_clientes.repositories;

import ec.edu.espe.logiflow.ms_clientes.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, UUID> {
    boolean existsByNumeroContrato(String numeroContrato);
    List<Cuenta> findByClienteId(UUID clienteId);
}