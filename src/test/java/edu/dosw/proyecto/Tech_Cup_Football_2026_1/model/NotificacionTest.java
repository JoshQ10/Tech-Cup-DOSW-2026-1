package edu.dosw.proyecto.Tech_Cup_Football_2026_1.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class NotificacionTest {

    @Test
    void testCrearNotificacion() {
        LocalDateTime fecha = LocalDateTime.now();
        Notificacion notif = new Notificacion(1L, 1L, "Mensaje importante", "ALERTA", false, fecha);
        
        assertEquals(1L, notif.getId());
        assertEquals(1L, notif.getUsuarioId());
        assertEquals("Mensaje importante", notif.getContenido());
        assertEquals("ALERTA", notif.getTipo());
        assertFalse(notif.isLeido());
        assertEquals(fecha, notif.getFechaEnvio());
    }

    @Test
    void testCrearNotificacionVacia() {
        Notificacion notif = new Notificacion();
        
        assertNull(notif.getId());
        assertNull(notif.getUsuarioId());
        assertNull(notif.getContenido());
        assertNull(notif.getTipo());
        assertFalse(notif.isLeido());
        assertNull(notif.getFechaEnvio());
    }

    @Test
    void testSettersNotificacion() {
        Notificacion notif = new Notificacion();
        LocalDateTime fecha = LocalDateTime.now();
        
        notif.setId(2L);
        notif.setUsuarioId(2L);
        notif.setContenido("Notificación de prueba");
        notif.setTipo("INFO");
        notif.setLeido(true);
        notif.setFechaEnvio(fecha);

        assertEquals(2L, notif.getId());
        assertEquals(2L, notif.getUsuarioId());
        assertEquals("Notificación de prueba", notif.getContenido());
        assertEquals("INFO", notif.getTipo());
        assertTrue(notif.isLeido());
        assertEquals(fecha, notif.getFechaEnvio());
    }
}
