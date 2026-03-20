package edu.dosw.proyecto.Tech_Cup_Football_2026_1.model;

import java.time.LocalDate;

public class Torneo {
    private String id;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String ubicacion;
    private int maxEquipos;
    private double costoInscripcion;
    private EstadoTorneo estado;

    public EstadoTorneo getEstado() {
        return estado;
    }

    public void setEstado(EstadoTorneo estado) {
        this.estado = estado;
    }
}

