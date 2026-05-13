package ec.edu.espe.logiflow.taller.restdos.services.impl;

import ec.edu.espe.logiflow.taller.restdos.dto.client.VehiculoFlotaResponse;
import ec.edu.espe.logiflow.taller.restdos.dto.request.OrdenMantenimientoRequest;
import ec.edu.espe.logiflow.taller.restdos.dto.response.OrdenMantenimientoResponse;
import ec.edu.espe.logiflow.taller.restdos.dto.response.VehiculoTallerResponse;
import ec.edu.espe.logiflow.taller.restdos.models.OrdenMantenimiento;
import ec.edu.espe.logiflow.taller.restdos.repositories.OrdenMantenimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TallerRestDosServiceImplTest {

    @Mock
    private OrdenMantenimientoRepository ordenMantenimientoRepository;

    @Mock
    private WebClient flotaWebClient;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private TallerRestDosServiceImpl tallerService;

    private VehiculoFlotaResponse vehiculoFlota;
    private OrdenMantenimientoRequest ordenRequest;
    private UUID vehiculoId;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        vehiculoId = UUID.randomUUID();

        vehiculoFlota = new VehiculoFlotaResponse();
        vehiculoFlota.setId(vehiculoId);
        vehiculoFlota.setMatricula("PBB-1234");
        vehiculoFlota.setEstado("DISPONIBLE");
        vehiculoFlota.setCapacidadKg(1000.0);
        vehiculoFlota.setAutonomiaKm(200.0);

        ordenRequest = new OrdenMantenimientoRequest();
        ordenRequest.setMatricula("PBB-1234");
        ordenRequest.setDescripcion("Mantenimiento preventivo de frenos");
    }

    /**
     * Helper para configurar el mock del WebClient en peticiones GET
     */
    @SuppressWarnings("unchecked")
    private void setupWebClientGetMock(Object response) {
        when(flotaWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        // CORRECCIÓN AQUÍ: Se usa Mono.just() en lugar de Mono.of()
        when(responseSpec.bodyToMono(any(Class.class))).thenReturn(Mono.just(response));
    }


    /**
     * Helper para configurar el mock del WebClient en peticiones PUT
     */
    @SuppressWarnings("unchecked")
    private void setupWebClientPutMock() {
        when(flotaWebClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString(), any(UUID.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());
    }

    @Test
    void consultarVehiculo_DeberiaRetornarVehiculoTaller_CuandoExisteEnFlota() {
        setupWebClientGetMock(vehiculoFlota);

        VehiculoTallerResponse response = tallerService.consultarVehiculo("PBB-1234");

        assertNotNull(response);
        assertEquals("PBB-1234", response.getMatricula());
        assertTrue(response.getDisponibleParaMantenimiento());
    }

    @Test
    void registrarOrdenMantenimiento_DeberiaCrearOrdenYActualizarFlota() {
        setupWebClientGetMock(vehiculoFlota);
        when(ordenMantenimientoRepository.save(any(OrdenMantenimiento.class))).thenAnswer(i -> i.getArguments()[0]);
        setupWebClientPutMock();

        OrdenMantenimientoResponse response = tallerService.registrarOrdenMantenimiento(ordenRequest);

        assertNotNull(response);
        assertEquals("PBB-1234", response.getMatricula());
        verify(ordenMantenimientoRepository, times(1)).save(any(OrdenMantenimiento.class));
    }

    @Test
    void registrarOrdenMantenimiento_DeberiaLanzarExcepcion_CuandoFlotaFalla() {
        when(flotaWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(Class.class))).thenReturn(Mono.error(new RuntimeException("Error")));

        assertThrows(ResponseStatusException.class, () -> {
            tallerService.registrarOrdenMantenimiento(ordenRequest);
        });
    }
}