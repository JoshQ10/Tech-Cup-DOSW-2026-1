package edu.dosw.proyecto.Tech_Cup_Football_2026_1.model;

import java.time.LocalDateTime;

public class Notificacion {
    private Long id;
    private Long usuarioId;
    private String contenido;
    private String tipo;
    private boolean leido;
    private LocalDateTime fechaEnvio;

    public Notificacion() {
    }

    public Notificacion(Long id, Long usuarioId, String contenido, String tipo, boolean leido, LocalDateTime fechaEnvio) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.contenido = contenido;
        this.tipo = tipo;
        this.leido = leido;
        this.fechaEnvio = fechaEnvio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", contenido='" + contenido + '\'' +
                ", tipo='" + tipo + '\'' +
                ", leido=" + leido +
                '}';
    }
}
