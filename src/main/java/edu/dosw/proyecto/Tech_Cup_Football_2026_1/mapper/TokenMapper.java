package edu.dosw.proyecto.Tech_Cup_Football_2026_1.mapper;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.TokenVerificacion;

public class TokenMapper {

    public static String toSimpleString(TokenVerificacion token) {
        return token.getToken();
    }
}