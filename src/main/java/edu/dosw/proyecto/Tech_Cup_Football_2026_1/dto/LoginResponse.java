package edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto;

public class LoginResponse {
    private String token;
    private Long usuarioId;
    private String email;
    private String rol;

    public LoginResponse(String token, Long usuarioId, String email, String rol) {
        this.token = token;
        this.usuarioId = usuarioId;
        this.email = email;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }
}