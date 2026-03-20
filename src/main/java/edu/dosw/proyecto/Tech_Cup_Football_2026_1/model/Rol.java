package edu.dosw.proyecto.Tech_Cup_Football_2026_1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Rol {

    public enum RolNombre {
        JUGADOR,
        ARBITRO,
        ADMINISTRADOR,
        ORGANIZADOR,
        CAPITAN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RolNombre nombre;

    public Rol() {
    }

    public Rol(RolNombre nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RolNombre getNombre() {
        return nombre;
    }

    public void setNombre(RolNombre nombre) {
        this.nombre = nombre;
    }
}
