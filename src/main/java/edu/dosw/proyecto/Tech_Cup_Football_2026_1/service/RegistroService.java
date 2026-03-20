package edu.dosw.proyecto.Tech_Cup_Football_2026_1.service;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RegistroRequest;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RegistroResponse;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Rol;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.TokenVerificacion;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Usuario;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository.RolRepository;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository.TokenVerificacionRepository;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository.UsuarioRepository;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.util.PasswordEncoderUtil;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.validator.EmailValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistroService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final TokenVerificacionRepository tokenVerificacionRepository;
    private final EmailService emailService;

    public RegistroService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            TokenVerificacionRepository tokenVerificacionRepository,
            EmailService emailService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.tokenVerificacionRepository = tokenVerificacionRepository;
        this.emailService = emailService;
    }

    @Transactional
    public RegistroResponse registrarUsuario(RegistroRequest solicitud) {
        if (solicitud == null) {
            return error("La solicitud de registro es obligatoria");
        }

        if (solicitud.getNombre() == null || solicitud.getNombre().trim().isEmpty()) {
            return error("El nombre es requerido");
        }

        if (solicitud.getTipoParticipante() == null) {
            return error("El tipo de participante es obligatorio");
        }

        if (!EmailValidator.validar(solicitud.getEmail())) {
            return error("El email no es válido");
        }

        if (!EmailValidator.cumpleReglaPorTipo(solicitud.getEmail(), solicitud.getTipoParticipante())) {
            return error("El correo no cumple las reglas para el tipo de participante");
        }

        if (usuarioRepository.existsByEmail(solicitud.getEmail())) {
            return error("Ya existe un usuario registrado con ese correo");
        }

        if (solicitud.getPassword() == null || solicitud.getPassword().length() < 8) {
            return error("La contraseña debe tener mínimo 8 caracteres");
        }

        if (!solicitud.getPassword().equals(solicitud.getPasswordConfirmacion())) {
            return error("Las contraseñas no coinciden");
        }

        Optional<Rol> rolJugador = rolRepository.findByNombre(Rol.RolNombre.JUGADOR);
        if (rolJugador.isEmpty()) {
            return error("No existe el rol base JUGADOR. Verifique la inicialización de roles.");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(solicitud.getNombre().trim());
        usuario.setEmail(solicitud.getEmail().trim().toLowerCase());
        usuario.setPassword(PasswordEncoderUtil.encode(solicitud.getPassword()));
        usuario.setActivo(true);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setEmailVerificado(false);
        usuario.setTipoParticipante(solicitud.getTipoParticipante());
        usuario.setRol(rolJugador.get());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        String token = UUID.randomUUID().toString();
        tokenVerificacionRepository.save(new TokenVerificacion(
                token,
                usuarioGuardado.getId(),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(24)
        ));

        String asunto = "Verifica tu correo - Tech Cup Football 2026";
        String cuerpo = "Haz clic en el siguiente enlace para verificar tu correo: " +
                "http://localhost:8080/api/auth/verificar?token=" + token;
        emailService.enviarEmail(usuarioGuardado.getEmail(), asunto, cuerpo);

        return new RegistroResponse(
                usuarioGuardado.getId(),
                usuarioGuardado.getNombre(),
                usuarioGuardado.getEmail(),
                usuarioGuardado.getRol().getNombre().name(),
                usuarioGuardado.getTipoParticipante().name(),
                "Usuario registrado exitosamente. Verifica tu correo",
                true
        );
    }

    private RegistroResponse error(String mensaje) {
        return new RegistroResponse(null, null, null, null, null, mensaje, false);
    }
}
