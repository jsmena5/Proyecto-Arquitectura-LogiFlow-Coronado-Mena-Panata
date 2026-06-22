package ec.edu.espe.logiflow.flota.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.logiflow.flota.dto.request.ConductorCreateRequest;
import ec.edu.espe.logiflow.flota.dto.response.ConductorResponse;
import ec.edu.espe.logiflow.flota.services.ConductorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConductorController.class)
class ConductorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConductorService conductorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createConductor_DeberiaRetornar201() throws Exception {
        ConductorCreateRequest request = new ConductorCreateRequest();
        request.setDni("1712345678");
        request.setPrimerNombre("Michael");
        request.setApellido("Coronado");
        request.setNumeroLicencia("ABC-12345");
        request.setTipoLicencia("C");

        ConductorResponse response = ConductorResponse.builder()
                .id(UUID.randomUUID())
                .dni("1712345678")
                .primerNombre("Michael")
                .apellido("Coronado")
                .numeroLicencia("ABC-12345")
                .tipoLicencia("C")
                .activo(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(conductorService.createConductor(any(ConductorCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/conductores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dni").value("1712345678"));
    }
}