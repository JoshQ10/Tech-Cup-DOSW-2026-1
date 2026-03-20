package edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.TipoParticipante;

public class RegistroRequest {
    private String nombre;
    private String email;
    private String password;
    private String passwordConfirmacion;
    private TipoParticipante tipoParticipante;

    public RegistroRequest() {
    }

    public RegistroRequest(String nombre, String email, String password, String passwordConfirmacion) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.passwordConfirmacion = passwordConfirmacion;
    }

    public RegistroRequest(String nombre, String email, String password, String passwordConfirmacion, TipoParticipante tipoParticipante) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.passwordConfirmacion = passwordConfirmacion;
        this.tipoParticipante = tipoParticipante;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmacion() {
        return passwordConfirmacion;
    }

    public void setPasswordConfirmacion(String passwordConfirmacion) {
        this.passwordConfirmacion = passwordConfirmacion;
    }

    public TipoParticipante getTipoParticipante() {
        return tipoParticipante;
    }

    public void setTipoParticipante(TipoParticipante tipoParticipante) {
        this.tipoParticipante = tipoParticipante;
    }
}

