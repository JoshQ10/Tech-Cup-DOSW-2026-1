package edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto;

public class RegistroResponse {
    private Long usuarioId;
    private String nombre;
    private String email;
    private String rol;
    private String tipoParticipante;
    private String mensaje;
    private boolean exitoso;

    public RegistroResponse() {
    }

    public RegistroResponse(Long usuarioId, String nombre, String email, String mensaje, boolean exitoso) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.email = email;
        this.mensaje = mensaje;
        this.exitoso = exitoso;
    }

    public RegistroResponse(Long usuarioId, String nombre, String email, String rol, String tipoParticipante, String mensaje, boolean exitoso) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.tipoParticipante = tipoParticipante;
        this.mensaje = mensaje;
        this.exitoso = exitoso;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTipoParticipante() {
        return tipoParticipante;
    }

    public void setTipoParticipante(String tipoParticipante) {
        this.tipoParticipante = tipoParticipante;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }
}

