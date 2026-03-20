package edu.dosw.proyecto.Tech_Cup_Football_2026_1.mapper;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.LoginResponse;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Usuario;

public class LoginMapper {

    public static LoginResponse toLoginResponse(String token, Usuario usuario) {
        return new LoginResponse(
                token,
                usuario.getId(),
                usuario.getEmail(),
                usuario.getRol().getNombre().name()
        );
    }
}