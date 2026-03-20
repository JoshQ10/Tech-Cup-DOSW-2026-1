package edu.dosw.proyecto.Tech_Cup_Football_2026_1.service;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.TokenVerificacion;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Usuario;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository.TokenVerificacionRepository;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository.UsuarioRepository;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.util.TokenGeneratorUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerificationService {

    private final UsuarioRepository usuarioRepository;
    private final TokenVerificacionRepository tokenRepository;
    private final TokenGeneratorUtil tokenGeneratorUtil;

    public VerificationService(UsuarioRepository usuarioRepository,
                               TokenVerificacionRepository tokenRepository,
                               TokenGeneratorUtil tokenGeneratorUtil) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.tokenGeneratorUtil = tokenGeneratorUtil;
    }

    public TokenVerificacion generarTokenParaUsuario(Long usuarioId) {
        TokenVerificacion token = tokenGeneratorUtil.generarTokenVerificacion(usuarioId);
        tokenRepository.save(token);
        return token;
    }

    public TokenVerificacion reenviarToken(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        tokenRepository.findAllByUsuarioId(usuarioId)
                .forEach(t -> {
                    t.marcarComoUsado();
                    tokenRepository.save(t);
                });

        TokenVerificacion nuevo = tokenGeneratorUtil.generarTokenVerificacion(usuarioId);
        tokenRepository.save(nuevo);

        return nuevo;
    }

    public String verificarToken(String token) {
        TokenVerificacion tokenEntidad = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (tokenEntidad.isUsado()) {
            throw new RuntimeException("El token ya fue usado.");
        }

        if (tokenEntidad.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El token ha expirado.");
        }

        tokenEntidad.marcarComoUsado();
        tokenRepository.save(tokenEntidad);

        Usuario usuario = usuarioRepository.findById(tokenEntidad.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setEmailVerificado(true);
        usuarioRepository.save(usuario);

        return "Correo verificado correctamente";
    }
}