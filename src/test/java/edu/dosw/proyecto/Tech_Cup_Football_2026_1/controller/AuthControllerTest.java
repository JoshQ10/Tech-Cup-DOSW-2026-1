package edu.dosw.proyecto.Tech_Cup_Football_2026_1.controller;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RegistroRequest;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RegistroResponse;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Rol;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.TipoParticipante;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.service.RegistroService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private RegistroService registroService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testRegistroExitoso() {
        RegistroRequest solicitud = new RegistroRequest(
                "Carlos Uribe",
                "carlos@escuelaing.edu.co",
                "Password123",
                "Password123",
                TipoParticipante.ESTUDIANTE,
                Rol.RolNombre.JUGADOR,
                null
        );

        RegistroResponse exito = new RegistroResponse(
                1L,
                "Carlos Uribe",
                "carlos@escuelaing.edu.co",
                "JUGADOR",
                "ESTUDIANTE",
                "Usuario registrado exitosamente",
                true
        );
        when(registroService.registrarUsuario(solicitud)).thenReturn(exito);

        ResponseEntity<RegistroResponse> respuesta = authController.registrar(solicitud);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertTrue(respuesta.getBody().isExitoso());
        assertEquals("carlos@escuelaing.edu.co", respuesta.getBody().getEmail());
    }

    @Test
    void testRegistroConEmailInvalido() {
        RegistroRequest solicitud = new RegistroRequest(
                "Carlos Uribe",
                "emailInvalido",
                "Password123",
                "Password123",
                TipoParticipante.ESTUDIANTE,
                Rol.RolNombre.JUGADOR,
                null
        );

        RegistroResponse error = new RegistroResponse(null, null, null, null, null, "El email no es valido", false);
        when(registroService.registrarUsuario(solicitud)).thenReturn(error);

        ResponseEntity<RegistroResponse> respuesta = authController.registrar(solicitud);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertFalse(respuesta.getBody().isExitoso());
    }

    @Test
    void testVerificarEmail() {
        ResponseEntity<String> respuesta = authController.verificarEmail("test-token-123");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("Email verificado exitosamente", respuesta.getBody());
    }
}
