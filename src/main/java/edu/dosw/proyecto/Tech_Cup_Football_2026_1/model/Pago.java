package edu.dosw.proyecto.Tech_Cup_Football_2026_1.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pago {
    private Long id;
    private Long usuarioId;
    private BigDecimal monto;
    private String descripcion;
    private String estado;
    private LocalDateTime fecha;
    private String metodoPago;

    public Pago() {
    }

    public Pago(Long id, Long usuarioId, BigDecimal monto, String descripcion, String estado, LocalDateTime fecha, String metodoPago) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.monto = monto;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
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

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public EstadoPago getEstadoPago() {
        if (estado == null) {
            return null;
        }
        return EstadoPago.valueOf(estado);
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estado = estadoPago == null ? null : estadoPago.name();
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", monto=" + monto +
                ", estado='" + estado + '\'' +
                ", metodoPago='" + metodoPago + '\'' +
                '}';
    }
}
