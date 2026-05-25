package ec.edu.espe.logiflow.taller.restdos.config;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleValidation() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
                mock(MethodParameter.class),
                bindingResult
        );

        ResponseEntity<Map<String, Object>> response = handler.handleValidation(ex);
        assertNotNull(response);
    }
}