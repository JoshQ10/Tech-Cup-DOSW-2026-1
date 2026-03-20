package edu.dosw.proyecto.Tech_Cup_Football_2026_1.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "token_verificacion")
public class TokenVerificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Long usuarioId;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaExpiracion;

    private boolean usado;

    public TokenVerificacion() {}

    public TokenVerificacion(String token, Long usuarioId, LocalDateTime fechaCreacion, LocalDateTime fechaExpiracion) {
        this.token = token;
        this.usuarioId = usuarioId;
        this.fechaCreacion = fechaCreacion;
        this.fechaExpiracion = fechaExpiracion;
        this.usado = false;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public boolean isUsado() {
        return usado;
    }

    public void marcarComoUsado() {
        this.usado = true;
    }
}