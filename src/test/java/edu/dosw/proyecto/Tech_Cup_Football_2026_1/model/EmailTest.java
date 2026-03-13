package edu.dosw.proyecto.Tech_Cup_Football_2026_1.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void testCrearEmail() {
        Email email = new Email(1L, "user@example.com", "Asunto", "Contenido", "admin@example.com", false);
        
        assertEquals(1L, email.getId());
        assertEquals("user@example.com", email.getDestinatario());
        assertEquals("Asunto", email.getAsunto());
        assertEquals("Contenido", email.getCuerpo());
        assertEquals("admin@example.com", email.getRemitente());
        assertFalse(email.isEnviado());
    }

    @Test
    void testCrearEmailVacio() {
        Email email = new Email();
        
        assertNull(email.getId());
        assertNull(email.getDestinatario());
        assertNull(email.getAsunto());
        assertNull(email.getCuerpo());
        assertNull(email.getRemitente());
        assertFalse(email.isEnviado());
    }

    @Test
    void testSettersEmail() {
        Email email = new Email();
        email.setId(2L);
        email.setDestinatario("test@example.com");
        email.setAsunto("Test Subject");
        email.setCuerpo("Test Content");
        email.setRemitente("sender@example.com");
        email.setEnviado(true);

        assertEquals(2L, email.getId());
        assertEquals("test@example.com", email.getDestinatario());
        assertEquals("Test Subject", email.getAsunto());
        assertEquals("Test Content", email.getCuerpo());
        assertEquals("sender@example.com", email.getRemitente());
        assertTrue(email.isEnviado());
    }
}
