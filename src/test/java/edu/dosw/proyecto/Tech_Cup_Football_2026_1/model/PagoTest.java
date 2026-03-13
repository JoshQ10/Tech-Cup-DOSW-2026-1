package edu.dosw.proyecto.Tech_Cup_Football_2026_1.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PagoTest {

    @Test
    void testCrearPago() {
        LocalDateTime fecha = LocalDateTime.now();
        Pago pago = new Pago(1L, 1L, new BigDecimal("100.00"), "Inscripción", "COMPLETADO", fecha, "TARJETA");
        
        assertEquals(1L, pago.getId());
        assertEquals(1L, pago.getUsuarioId());
        assertEquals(new BigDecimal("100.00"), pago.getMonto());
        assertEquals("Inscripción", pago.getDescripcion());
        assertEquals("COMPLETADO", pago.getEstado());
        assertEquals(fecha, pago.getFecha());
        assertEquals("TARJETA", pago.getMetodoPago());
    }

    @Test
    void testCrearPagoVacio() {
        Pago pago = new Pago();
        
        assertNull(pago.getId());
        assertNull(pago.getUsuarioId());
        assertNull(pago.getMonto());
        assertNull(pago.getDescripcion());
        assertNull(pago.getEstado());
        assertNull(pago.getFecha());
        assertNull(pago.getMetodoPago());
    }

    @Test
    void testSettersPago() {
        Pago pago = new Pago();
        LocalDateTime fecha = LocalDateTime.now();
        
        pago.setId(2L);
        pago.setUsuarioId(2L);
        pago.setMonto(new BigDecimal("250.50"));
        pago.setDescripcion("Membresía");
        pago.setEstado("PENDIENTE");
        pago.setFecha(fecha);
        pago.setMetodoPago("TRANSFERENCIA");

        assertEquals(2L, pago.getId());
        assertEquals(2L, pago.getUsuarioId());
        assertEquals(new BigDecimal("250.50"), pago.getMonto());
        assertEquals("Membresía", pago.getDescripcion());
        assertEquals("PENDIENTE", pago.getEstado());
        assertEquals(fecha, pago.getFecha());
        assertEquals("TRANSFERENCIA", pago.getMetodoPago());
    }
}
