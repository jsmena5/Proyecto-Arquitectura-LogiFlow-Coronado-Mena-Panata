package ec.edu.espe.logiflow.ms_auth.services.impl;

import ec.edu.espe.logiflow.ms_auth.dto.request.AuthRequest;
import ec.edu.espe.logiflow.ms_auth.dto.request.RegisterRequest;
import ec.edu.espe.logiflow.ms_auth.dto.response.AuthResponse;
import ec.edu.espe.logiflow.ms_auth.models.Usuario;
import ec.edu.espe.logiflow.ms_auth.repositories.UsuarioRepository;
import ec.edu.espe.logiflow.ms_auth.security.JwtService;
import ec.edu.espe.logiflow.ms_auth.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Validación de usuario existente
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya está registrado");
        }

        // Creamos el usuario y ENCRIPTAMOS la contraseña antes de guardar
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .activo(true)
                .build();

        usuarioRepository.save(usuario);

        // Devolvemos el token inmediatamente después de registrarse
        return AuthResponse.builder()
                .token(jwtService.generarToken(usuario))
                .username(usuario.getUsername())
                .rol(usuario.getRol().name())
                .build();
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        // Buscamos si existe el usuario
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Comparamos la contraseña cruda con el Hash encriptado de la BD
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        // Si todo está bien, generamos su pase de acceso (Token JWT)
        return AuthResponse.builder()
                .token(jwtService.generarToken(usuario))
                .username(usuario.getUsername())
                .rol(usuario.getRol().name())
                .build();
    }
}