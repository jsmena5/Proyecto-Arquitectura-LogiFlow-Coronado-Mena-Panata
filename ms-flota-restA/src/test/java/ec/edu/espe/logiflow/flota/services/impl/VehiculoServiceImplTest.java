package ec.edu.espe.logiflow.flota.services.impl;

import ec.edu.espe.logiflow.flota.dto.request.VehiculoCreateRequest;
import ec.edu.espe.logiflow.flota.dto.request.VehiculoUpdateRequest;
import ec.edu.espe.logiflow.flota.dto.response.VehiculoResponse;
import ec.edu.espe.logiflow.flota.enums.EstadoVehiculo;
import ec.edu.espe.logiflow.flota.enums.TipoVehiculo;
import ec.edu.espe.logiflow.flota.models.Vehiculo;
import ec.edu.espe.logiflow.flota.repositories.VehiculoRepository;
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
class VehiculoServiceImplTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private VehiculoServiceImpl vehiculoService;

    private Vehiculo vehiculoFalso;
    private VehiculoCreateRequest createRequest;
    private VehiculoUpdateRequest updateRequest;
    private UUID vehiculoId;

    @BeforeEach
    void setUp() {
        // Se ejecuta antes de CADA prueba para tener datos limpios
        vehiculoId = UUID.randomUUID();

        vehiculoFalso = Vehiculo.builder()
                .id(vehiculoId)
                .matricula("ABC-1234")
                .tipo(TipoVehiculo.FURGONETA)
                .capacidadKg(1500.0)
                .autonomiaKm(300.0)
                .estado(EstadoVehiculo.DISPONIBLE)
                .createdAt(LocalDateTime.now())
                .build();

        createRequest = new VehiculoCreateRequest();
        createRequest.setMatricula("abc-1234");
        createRequest.setTipo(TipoVehiculo.FURGONETA);
        createRequest.setCapacidadKg(1500.0);
        createRequest.setAutonomiaKm(300.0);

        updateRequest = new VehiculoUpdateRequest();
        updateRequest.setEstado(EstadoVehiculo.MANTENIMIENTO);
        updateRequest.setCapacidadKg(2000.0);
        updateRequest.setAutonomiaKm(350.0);
    }

    @Test
    void createVehiculo_DeberiaCrearVehiculo_CuandoMatriculaNoExiste() {
        when(vehiculoRepository.existsByMatricula("ABC-1234")).thenReturn(false);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculoFalso);

        VehiculoResponse response = vehiculoService.createVehiculo(createRequest);

        assertNotNull(response);
        assertEquals("ABC-1234", response.getMatricula());
        assertEquals(TipoVehiculo.FURGONETA, response.getTipo());
        verify(vehiculoRepository, times(1)).save(any(Vehiculo.class));
    }

    @Test
    void createVehiculo_DeberiaLanzarExcepcion_CuandoMatriculaYaExiste() {
        when(vehiculoRepository.existsByMatricula("ABC-1234")).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            vehiculoService.createVehiculo(createRequest);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }

    @Test
    void updateVehiculo_DeberiaActualizarVehiculo_CuandoExiste() {
        when(vehiculoRepository.findById(vehiculoId)).thenReturn(Optional.of(vehiculoFalso));
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculoFalso);

        VehiculoResponse response = vehiculoService.updateVehiculo(vehiculoId, updateRequest);

        assertNotNull(response);
        verify(vehiculoRepository, times(1)).save(any(Vehiculo.class));
    }

    @Test
    void updateVehiculo_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(vehiculoRepository.findById(vehiculoId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            vehiculoService.updateVehiculo(vehiculoId, updateRequest);
        });
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }

    @Test
    void getVehiculoByMatricula_DeberiaRetornarVehiculo_CuandoExiste() {
        when(vehiculoRepository.findByMatricula("ABC-1234")).thenReturn(Optional.of(vehiculoFalso));

        VehiculoResponse response = vehiculoService.getVehiculoByMatricula("abc-1234");

        assertNotNull(response);
        assertEquals("ABC-1234", response.getMatricula());
    }

    @Test
    void getVehiculoByMatricula_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(vehiculoRepository.findByMatricula("ABC-1234")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            vehiculoService.getVehiculoByMatricula("abc-1234");
        });
    }

    @Test
    void getAllVehiculos_DeberiaRetornarListaDeVehiculos() {
        when(vehiculoRepository.findAll()).thenReturn(List.of(vehiculoFalso));

        List<VehiculoResponse> list = vehiculoService.getAllVehiculos();

        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    void getVehiculosDisponibles_DeberiaRetornarListaDeVehiculosDisponibles() {
        when(vehiculoRepository.findByEstado(EstadoVehiculo.DISPONIBLE)).thenReturn(List.of(vehiculoFalso));

        List<VehiculoResponse> list = vehiculoService.getVehiculosDisponibles();

        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    void deleteVehiculo_DeberiaEliminarVehiculo_CuandoExiste() {
        when(vehiculoRepository.existsById(vehiculoId)).thenReturn(true);

        assertDoesNotThrow(() -> vehiculoService.deleteVehiculo(vehiculoId));
        verify(vehiculoRepository, times(1)).deleteById(vehiculoId);
    }

    @Test
    void deleteVehiculo_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(vehiculoRepository.existsById(vehiculoId)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> {
            vehiculoService.deleteVehiculo(vehiculoId);
        });
        verify(vehiculoRepository, never()).deleteById(any());
    }
}