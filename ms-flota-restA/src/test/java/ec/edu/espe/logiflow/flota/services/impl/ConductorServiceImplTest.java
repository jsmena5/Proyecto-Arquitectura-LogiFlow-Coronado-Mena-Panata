package ec.edu.espe.logiflow.flota.services.impl;

import ec.edu.espe.logiflow.flota.dto.request.ConductorCreateRequest;
import ec.edu.espe.logiflow.flota.dto.request.ConductorUpdateRequest;
import ec.edu.espe.logiflow.flota.dto.response.ConductorResponse;
import ec.edu.espe.logiflow.flota.models.Conductor;
import ec.edu.espe.logiflow.flota.repositories.ConductorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConductorServiceImplTest {

    @Mock
    private ConductorRepository conductorRepository;

    @InjectMocks
    private ConductorServiceImpl conductorService;

    private Conductor conductorFalso;
    private ConductorCreateRequest createRequest;
    private ConductorUpdateRequest updateRequest;
    private UUID conductorId;

    @BeforeEach
    void setUp() {
        conductorId = UUID.randomUUID();

        // Simulamos un conductor guardado en la Base de Datos
        conductorFalso = Conductor.builder()
                .id(conductorId)
                .dni("1712345678")
                .primerNombre("Michael")
                .apellido("Coronado")
                .numeroLicencia("ABC-12345")
                .tipoLicencia("C")
                .activo(true)
                .createdAt(LocalDateTime.now())
                .build();

        // Simulamos la petición que llega del controlador para CREAR
        createRequest = new ConductorCreateRequest();
        createRequest.setDni("1712345678");
        createRequest.setPrimerNombre("Michael");
        createRequest.setApellido("Coronado");
        createRequest.setNumeroLicencia("abc-12345");
        createRequest.setTipoLicencia("c");

        // Simulamos la petición que llega del controlador para ACTUALIZAR
        updateRequest = new ConductorUpdateRequest();
        updateRequest.setPrimerNombre("Andres");
        updateRequest.setApellido("Achig");
        updateRequest.setTipoLicencia("E");
        updateRequest.setActivo(false);
    }

    @Test
    void createConductor_DeberiaCrearConductor_CuandoDatosSonNuevos() {
        when(conductorRepository.existsByDni("1712345678")).thenReturn(false);
        when(conductorRepository.existsByNumeroLicencia("ABC-12345")).thenReturn(false);
        when(conductorRepository.save(any(Conductor.class))).thenReturn(conductorFalso);

        ConductorResponse response = conductorService.createConductor(createRequest);

        assertNotNull(response);
        assertEquals("1712345678", response.getDni());
        verify(conductorRepository, times(1)).save(any(Conductor.class));
    }

    @Test
    void createConductor_DeberiaLanzarExcepcion_CuandoDniYaExiste() {
        when(conductorRepository.existsByDni("1712345678")).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            conductorService.createConductor(createRequest);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("El DNI ya está registrado", exception.getReason());
        verify(conductorRepository, never()).save(any(Conductor.class));
    }

    @Test
    void createConductor_DeberiaLanzarExcepcion_CuandoLicenciaYaExiste() {
        when(conductorRepository.existsByDni("1712345678")).thenReturn(false);
        when(conductorRepository.existsByNumeroLicencia("ABC-12345")).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            conductorService.createConductor(createRequest);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("La licencia ya está registrada", exception.getReason());
        verify(conductorRepository, never()).save(any(Conductor.class));
    }

    @Test
    void updateConductor_DeberiaActualizar_CuandoExiste() {
        when(conductorRepository.findById(conductorId)).thenReturn(Optional.of(conductorFalso));
        when(conductorRepository.save(any(Conductor.class))).thenReturn(conductorFalso);

        ConductorResponse response = conductorService.updateConductor(conductorId, updateRequest);

        assertNotNull(response);
        verify(conductorRepository, times(1)).save(any(Conductor.class));
    }

    @Test
    void updateConductor_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(conductorRepository.findById(conductorId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            conductorService.updateConductor(conductorId, updateRequest);
        });
        verify(conductorRepository, never()).save(any(Conductor.class));
    }

    @Test
    void getAllConductores_DeberiaRetornarLista() {
        when(conductorRepository.findAll()).thenReturn(List.of(conductorFalso));

        List<ConductorResponse> list = conductorService.getAllConductores();

        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        verify(conductorRepository, times(1)).findAll();
    }

    @Test
    void deleteConductor_DeberiaEliminar_CuandoExiste() {
        when(conductorRepository.existsById(conductorId)).thenReturn(true);

        assertDoesNotThrow(() -> conductorService.deleteConductor(conductorId));
        verify(conductorRepository, times(1)).deleteById(conductorId);
    }

    @Test
    void deleteConductor_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(conductorRepository.existsById(conductorId)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> {
            conductorService.deleteConductor(conductorId);
        });
        verify(conductorRepository, never()).deleteById(any());
    }
}