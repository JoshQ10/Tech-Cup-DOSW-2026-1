package edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto;

import java.time.LocalDateTime;

public class RespuestaError {
    private int codigo;
    private String mensaje;
    private String detalles;
    private LocalDateTime timestamp;

    public RespuestaError(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.timestamp = LocalDateTime.now();
    }

    public RespuestaError(int codigo, String mensaje, String detalles) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.detalles = detalles;
        this.timestamp = LocalDateTime.now();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
