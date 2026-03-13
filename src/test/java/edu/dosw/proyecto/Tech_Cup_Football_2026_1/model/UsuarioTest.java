package edu.dosw.proyecto.Tech_Cup_Football_2026_1.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testCrearUsuario() {
        Usuario usuario = new Usuario(1L, "Juan", "juan@example.com", "password123", true);
        
        assertEquals(1L, usuario.getId());
        assertEquals("Juan", usuario.getNombre());
        assertEquals("juan@example.com", usuario.getEmail());
        assertEquals("password123", usuario.getPassword());
        assertTrue(usuario.isActivo());
    }

    @Test
    void testCrearUsuarioVacio() {
        Usuario usuario = new Usuario();
        
        assertNull(usuario.getId());
        assertNull(usuario.getNombre());
        assertNull(usuario.getEmail());
        assertNull(usuario.getPassword());
        assertFalse(usuario.isActivo());
    }

    @Test
    void testSettersUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setNombre("Carlos");
        usuario.setEmail("carlos@example.com");
        usuario.setPassword("pass456");
        usuario.setActivo(false);

        assertEquals(2L, usuario.getId());
        assertEquals("Carlos", usuario.getNombre());
        assertEquals("carlos@example.com", usuario.getEmail());
        assertEquals("pass456", usuario.getPassword());
        assertFalse(usuario.isActivo());
    }
}
