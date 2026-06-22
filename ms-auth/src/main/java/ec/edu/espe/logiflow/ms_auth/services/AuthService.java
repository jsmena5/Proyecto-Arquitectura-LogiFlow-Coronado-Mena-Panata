package ec.edu.espe.logiflow.ms_auth.services;

import ec.edu.espe.logiflow.ms_auth.dto.request.AuthRequest;
import ec.edu.espe.logiflow.ms_auth.dto.request.RegisterRequest;
import ec.edu.espe.logiflow.ms_auth.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(AuthRequest request);
}