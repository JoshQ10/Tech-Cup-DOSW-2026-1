package edu.dosw.proyecto.Tech_Cup_Football_2026_1.model;

import java.util.ArrayList;
import java.util.List;

public class Alineacion {
    private String id;
    private String partidoId;
    private String equipoId;
    private String formacion;
    private List<String> jugadoresTitulares;
    private List<String> jugadoresReservas;
    private List<PosicionEnCancha> posicionesEnCancha;

    public Alineacion() {
        this.jugadoresTitulares = new ArrayList<>();
        this.jugadoresReservas = new ArrayList<>();
        this.posicionesEnCancha = new ArrayList<>();
    }

    public Alineacion(String id, String partidoId, String equipoId, String formacion) {
        this();
        this.id = id;
        this.partidoId = partidoId;
        this.equipoId = equipoId;
        this.formacion = formacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(String partidoId) {
        this.partidoId = partidoId;
    }

    public String getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(String equipoId) {
        this.equipoId = equipoId;
    }

    public String getFormacion() {
        return formacion;
    }

    public void setFormacion(String formacion) {
        this.formacion = formacion;
    }

    public List<String> getJugadoresTitulares() {
        return jugadoresTitulares;
    }

    public void setJugadoresTitulares(List<String> jugadoresTitulares) {
        this.jugadoresTitulares = jugadoresTitulares;
    }

    public List<String> getJugadoresReservas() {
        return jugadoresReservas;
    }

    public void setJugadoresReservas(List<String> jugadoresReservas) {
        this.jugadoresReservas = jugadoresReservas;
    }

    public List<PosicionEnCancha> getPosicionesEnCancha() {
        return posicionesEnCancha;
    }

    public void setPosicionesEnCancha(List<PosicionEnCancha> posicionesEnCancha) {
        this.posicionesEnCancha = posicionesEnCancha;
    }
}

