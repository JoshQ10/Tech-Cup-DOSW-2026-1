package edu.dosw.proyecto.Tech_Cup_Football_2026_1.service;

import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RegistroRequest;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.dto.RegistroResponse;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Rol;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.TipoParticipante;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.TokenVerificacion;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.model.Usuario;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository.RolRepository;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository.TokenVerificacionRepository;
import edu.dosw.proyecto.Tech_Cup_Football_2026_1.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private RegistroService registroService;

    @Test
    void registrarUsuario_exitosoJugadorInstitucional() {
        Rol rolJugador = new Rol(Rol.RolNombre.JUGADOR);
        when(rolRepository.findByNombre(Rol.RolNombre.JUGADOR)).thenReturn(Optional.of(rolJugador));
        when(usuarioRepository.existsByEmail("juan@escuelaing.edu.co")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(tokenVerificacionRepository.save(any(TokenVerificacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RegistroRequest solicitud = new RegistroRequest(
                "Juan Perez",
                "juan@escuelaing.edu.co",
                "Password123",
                "Password123",
                TipoParticipante.ESTUDIANTE,
                Rol.RolNombre.JUGADOR,
                null
        );

        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);

        assertTrue(respuesta.isExitoso());
        assertEquals("JUGADOR", respuesta.getRol());
        assertEquals("ESTUDIANTE", respuesta.getTipoParticipante());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(tokenVerificacionRepository, times(1)).save(any(TokenVerificacion.class));
        verify(emailService, times(1)).enviarEmail(anyString(), anyString(), anyString());
    }

    @Test
    void registrarUsuario_exitosoExternoConGmail() {
        Rol rolJugador = new Rol(Rol.RolNombre.JUGADOR);
        when(rolRepository.findByNombre(Rol.RolNombre.JUGADOR)).thenReturn(Optional.of(rolJugador));
        when(usuarioRepository.existsByEmail("externo@gmail.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(2L);
            return u;
        });
        when(tokenVerificacionRepository.save(any(TokenVerificacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RegistroRequest solicitud = new RegistroRequest(
                "Usuario Externo",
                "externo@gmail.com",
                "Password123",
                "Password123",
                TipoParticipante.EXTERNO,
                Rol.RolNombre.JUGADOR,
                "Aliado institucional"
        );

        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);

        assertTrue(respuesta.isExitoso());
        assertEquals("EXTERNO", respuesta.getTipoParticipante());

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());
        assertEquals("Aliado institucional", usuarioCaptor.getValue().getDescripcionRelacionExterna());
        verify(tokenVerificacionRepository, times(1)).save(any(TokenVerificacion.class));
        verify(emailService, times(1)).enviarEmail(anyString(), anyString(), anyString());
    }

    @Test
    void registrarUsuario_errorCuandoEmailYaExiste() {
        when(usuarioRepository.existsByEmail("ana@escuelaing.edu.co")).thenReturn(true);

        RegistroRequest solicitud = new RegistroRequest(
                "Ana",
                "ana@escuelaing.edu.co",
                "Password123",
                "Password123",
                TipoParticipante.ESTUDIANTE,
                Rol.RolNombre.JUGADOR,
                null
        );

        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);

        assertFalse(respuesta.isExitoso());
        assertTrue(respuesta.getMensaje().contains("Ya existe un usuario"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(tokenVerificacionRepository, never()).save(any(TokenVerificacion.class));
        verify(emailService, never()).enviarEmail(anyString(), anyString(), anyString());
    }

    @Test
    void registrarUsuario_errorCuandoEmailNoCumpleReglaPorTipo() {
        RegistroRequest solicitud = new RegistroRequest(
                "Ana",
                "ana@gmail.com",
                "Password123",
                "Password123",
                TipoParticipante.ESTUDIANTE,
                Rol.RolNombre.JUGADOR,
                null
        );

        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);

        assertFalse(respuesta.isExitoso());
        assertTrue(respuesta.getMensaje().contains("no cumple las reglas"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_errorCuandoContrasenasNoCoinciden() {
        RegistroRequest solicitud = new RegistroRequest(
                "Luis",
                "luis@escuelaing.edu.co",
                "Password123",
                "OtraPassword123",
                TipoParticipante.ESTUDIANTE,
                Rol.RolNombre.JUGADOR,
                null
        );

        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);

        assertFalse(respuesta.isExitoso());
        assertTrue(respuesta.getMensaje().contains("no coinciden"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_errorCuandoContrasenaMenorA8() {
        RegistroRequest solicitud = new RegistroRequest(
                "Luis",
                "luis@escuelaing.edu.co",
                "Pass12",
                "Pass12",
                TipoParticipante.ESTUDIANTE,
                Rol.RolNombre.JUGADOR,
                null
        );

        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);

        assertFalse(respuesta.isExitoso());
        assertTrue(respuesta.getMensaje().contains("mínimo 8"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_errorCuandoNombreVacio() {
        RegistroRequest solicitud = new RegistroRequest(
                "   ",
                "luis@escuelaing.edu.co",
                "Password123",
                "Password123",
                TipoParticipante.ESTUDIANTE,
                Rol.RolNombre.JUGADOR,
                null
        );

        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);

        assertFalse(respuesta.isExitoso());
        assertEquals("El nombre es requerido", respuesta.getMensaje());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_errorCuandoTipoParticipanteNulo() {
        RegistroRequest solicitud = new RegistroRequest(
                "Luis",
                "luis@escuelaing.edu.co",
                "Password123",
                "Password123"
        );
        solicitud.setRol(Rol.RolNombre.JUGADOR);

        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);

        assertFalse(respuesta.isExitoso());
        assertEquals("El tipo de participante es obligatorio", respuesta.getMensaje());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_errorCuandoRolJugadorNoExiste() {
        when(usuarioRepository.existsByEmail("juan@escuelaing.edu.co")).thenReturn(false);
        when(rolRepository.findByNombre(Rol.RolNombre.JUGADOR)).thenReturn(Optional.empty());

        RegistroRequest solicitud = new RegistroRequest(
                "Juan",
                "juan@escuelaing.edu.co",
                "Password123",
                "Password123",
                TipoParticipante.ESTUDIANTE,
                null,
                null
        );

        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);

        assertFalse(respuesta.isExitoso());
        assertNotNull(respuesta.getMensaje());
        assertTrue(respuesta.getMensaje().contains("rol solicitado"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_errorCuandoExternoSinDescripcionRelacionExterna() {
        when(usuarioRepository.existsByEmail("externo@gmail.com")).thenReturn(false);
        when(rolRepository.findByNombre(Rol.RolNombre.JUGADOR)).thenReturn(Optional.of(new Rol(Rol.RolNombre.JUGADOR)));

        RegistroRequest solicitud = new RegistroRequest(
                "Externo",
                "externo@gmail.com",
                "Password123",
                "Password123",
                TipoParticipante.EXTERNO,
                Rol.RolNombre.JUGADOR,
                "   "
        );

        RegistroResponse respuesta = registroService.registrarUsuario(solicitud);

        assertFalse(respuesta.isExitoso());
        assertTrue(respuesta.getMensaje().contains("externos deben especificar"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(tokenVerificacionRepository, never()).save(any(TokenVerificacion.class));
        verify(emailService, never()).enviarEmail(anyString(), anyString(), anyString());
    }
}
