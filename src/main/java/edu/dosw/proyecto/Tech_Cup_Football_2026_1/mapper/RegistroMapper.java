package edu.dosw.proyecto.Tech_Cup_Football_2026_1.mapper;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RegistroResponse;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Usuario;

public class RegistroMapper {

    public static RegistroResponse toResponse(Usuario usuario, String mensaje, boolean exitoso) {
        return new RegistroResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().getNombre().name(),
                usuario.getTipoParticipante().name(),
                mensaje,
                exitoso
        );
    }
}