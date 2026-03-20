package edu.dosw.proyecto.Tech_Cup_Football_2026_1.mapper;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RegistroRequest;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Usuario;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Rol;

public class UsuarioMapper {

    public static Usuario toUsuario(RegistroRequest request, Rol rol, String passwordEncriptado) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncriptado);
        usuario.setTipoParticipante(request.getTipoParticipante());
        usuario.setActivo(true);
        usuario.setRol(rol);
        return usuario;
    }
}