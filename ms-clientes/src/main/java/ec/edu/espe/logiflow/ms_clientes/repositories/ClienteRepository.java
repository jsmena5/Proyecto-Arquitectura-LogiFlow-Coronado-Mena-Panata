package ec.edu.espe.logiflow.ms_clientes.repositories;

import ec.edu.espe.logiflow.ms_clientes.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    boolean existsByIdentificacion(String identificacion);
    boolean existsByCorreo(String correo);
}