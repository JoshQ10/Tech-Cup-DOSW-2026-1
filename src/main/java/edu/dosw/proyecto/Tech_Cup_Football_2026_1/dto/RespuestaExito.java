package edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto;

public class RespuestaExito<T> {
    private boolean exito;
    private String mensaje;
    private T datos;

    public RespuestaExito(String mensaje, T datos) {
        this.exito = true;
        this.mensaje = mensaje;
        this.datos = datos;
    }

    public RespuestaExito(String mensaje) {
        this.exito = true;
        this.mensaje = mensaje;
        this.datos = null;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public T getDatos() {
        return datos;
    }

    public void setDatos(T datos) {
        this.datos = datos;
    }
}
