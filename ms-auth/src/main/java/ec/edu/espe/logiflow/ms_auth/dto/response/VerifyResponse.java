package ec.edu.espe.logiflow.ms_auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyResponse {
    private boolean valid;
    private String username;
    private String rol;
}