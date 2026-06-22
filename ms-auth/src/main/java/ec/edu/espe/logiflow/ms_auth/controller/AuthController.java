package ec.edu.espe.logiflow.ms_auth.controller;

import ec.edu.espe.logiflow.ms_auth.dto.request.AuthRequest;
import ec.edu.espe.logiflow.ms_auth.dto.request.RegisterRequest;
import ec.edu.espe.logiflow.ms_auth.dto.response.AuthResponse;
import ec.edu.espe.logiflow.ms_auth.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.logiflow.ms_auth.dto.response.VerifyResponse;
import ec.edu.espe.logiflow.ms_auth.security.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para registro y login de usuarios con JWT")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión para obtener el token JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/verify")
    @Operation(summary = "Verificar la validez de un token JWT")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<VerifyResponse> verifyToken(@Parameter(hidden = true)@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        // Verificamos que el header exista y empiece con "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(VerifyResponse.builder().valid(false).build());
        }
        // Extraemos solo el token (quitamos la palabra "Bearer ")
        String token = authHeader.substring(7);

        if (jwtService.isTokenValid(token)) {
            Claims claims = jwtService.extraerClaims(token);
            return ResponseEntity.ok(VerifyResponse.builder()
                    .valid(true)
                    .username(claims.getSubject())
                    .rol(claims.get("rol", String.class))
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(VerifyResponse.builder().valid(false).build());
        }
    }
}