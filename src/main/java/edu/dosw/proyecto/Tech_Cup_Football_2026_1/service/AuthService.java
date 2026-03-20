package edu.dosw.proyecto.Tech_Cup_Football_2026_1.service;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.LoginRequest;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.LoginResponse;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Usuario;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository.UsuarioRepository;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.util.JwtUtil;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.util.PasswordEncoderUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!PasswordEncoderUtil.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        if (!usuario.isEmailVerificado()) {
            throw new RuntimeException("Debe verificar su correo antes de iniciar sesión");
        }

        String token = jwtUtil.generarToken(usuario.getEmail());

        return new LoginResponse(
                token,
                usuario.getId(),
                usuario.getEmail(),
                usuario.getRol().getNombre().name()
        );
    }
}