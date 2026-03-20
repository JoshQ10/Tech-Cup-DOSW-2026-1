package edu.dosw.proyecto.Tech_Cup_Football_2026_1.util;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.TokenVerificacion;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TokenGeneratorUtil {

    private static final int MINUTOS_EXPIRACION = 15;

    public TokenVerificacion generarTokenVerificacion(Long idUsuario) {

        String token = UUID.randomUUID().toString();

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime expiracion = ahora.plusMinutes(MINUTOS_EXPIRACION);

        return new TokenVerificacion(
                token,
                idUsuario,
                ahora,
                expiracion
        );
    }
}