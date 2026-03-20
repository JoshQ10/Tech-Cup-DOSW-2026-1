package edu.dosw.proyecto.Tech_Cup_Football_2026_1.service;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RegistroRequest;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RegistroResponse;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Rol;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.TipoParticipante;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Usuario;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository.RolRepository;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository.TokenVerificacionRepository;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistroServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private TokenVerificacionRepository tokenVerificacionRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private VerificationService verificationService;

    private RegistroService registroService;

    @BeforeEach
    void setUp() {
        registroService = new RegistroService(
                usuarioRepository,
                rolRepository,
                tokenVerificacionRepository,
                emailService,
                verificationService
        );
    }

    @Test
    void testRegistroExitosoInstitucional() {
        Rol rolJugador = new Rol(Rol.RolNombre.JUGADOR);
        when(rolRepository.findByNombre(Rol.RolNombre.JUGADOR)).thenReturn(Optional.of(rolJugador));
        when(usuarioRepository.existsByEmail("juan@escuelaing.edu.co")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        RegistroRequest solicitud = new RegistroRequest(
                "Juan Perez",
                "juan@escuelaing.edu.co",
                "Password123",
                "Password123",
                TipoParticipante.ESTUDIANTE
        );

        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);

        assertTrue(respuesta.isExitoso());
        assertEquals("JUGADOR", respuesta.getRol());
        assertEquals("ESTUDIANTE", respuesta.getTipoParticipante());
        verify(tokenVerificacionRepository, times(1)).save(any());
        verify(emailService, times(1)).enviarEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testRegistroFamiliarConCorreoNoGmail() {
        RegistroRequest solicitud = new RegistroRequest(
                "Familiar Uno",
                "familiar@outlook.com",
                "Password123",
                "Password123",
                TipoParticipante.FAMILIAR
        );

        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);

        assertFalse(respuesta.isExitoso());
        assertTrue(respuesta.getMensaje().contains("no cumple las reglas"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testRegistroEstudianteConCorreoNoInstitucional() {
        RegistroRequest solicitud = new RegistroRequest(
                "Ana",
                "ana@gmail.com",
                "Password123",
                "Password123",
                TipoParticipante.ESTUDIANTE
        );

        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);

        assertFalse(respuesta.isExitoso());
        assertTrue(respuesta.getMensaje().contains("no cumple las reglas"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testRegistroConCorreoDuplicado() {
        when(usuarioRepository.existsByEmail("ana@escuelaing.edu.co")).thenReturn(true);

        RegistroRequest solicitud = new RegistroRequest(
                "Ana",
                "ana@escuelaing.edu.co",
                "Password123",
                "Password123",
                TipoParticipante.ADMINISTRATIVO
        );

        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);

        assertFalse(respuesta.isExitoso());
        assertTrue(respuesta.getMensaje().contains("Ya existe un usuario"));
    }

    @Test
    void testRegistroSinRolSemilla() {
        when(usuarioRepository.existsByEmail("ana@escuelaing.edu.co")).thenReturn(false);
        when(rolRepository.findByNombre(Rol.RolNombre.JUGADOR)).thenReturn(Optional.empty());

        RegistroRequest solicitud = new RegistroRequest(
                "Ana",
                "ana@escuelaing.edu.co",
                "Password123",
                "Password123",
                TipoParticipante.PROFESOR
        );

        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);

        assertFalse(respuesta.isExitoso());
        assertTrue(respuesta.getMensaje().contains("rol base JUGADOR"));
    }

    @Test
    void testGuardaUsuarioConRolJugador() {
        Rol rolJugador = new Rol(Rol.RolNombre.JUGADOR);
        when(rolRepository.findByNombre(Rol.RolNombre.JUGADOR)).thenReturn(Optional.of(rolJugador));
        when(usuarioRepository.existsByEmail("carlos@escuelaing.edu.co")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(99L);
            return u;
        });

        RegistroRequest solicitud = new RegistroRequest(
                "Carlos",
                "carlos@escuelaing.edu.co",
                "Password123",
                "Password123",
                TipoParticipante.GRADUADO
        );

        registroService.registrarUsuario(solicitud);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertEquals(Rol.RolNombre.JUGADOR, captor.getValue().getRol().getNombre());
    }
}
