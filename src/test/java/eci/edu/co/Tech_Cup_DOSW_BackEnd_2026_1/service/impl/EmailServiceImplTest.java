package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.EmailServiceImpl;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailServiceImpl - Correos de activación de cuenta")
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "emailFrom", "noreply@techcup.edu.co");
        ReflectionTestUtils.setField(emailService, "verificationUrl", "http://localhost:3000/verify-email");
        ReflectionTestUtils.setField(emailService, "resetPasswordUrl", "http://localhost:3000/reset-password");
    }

    @Test
    @DisplayName("Debe enviar el email al destinatario correcto con el asunto esperado")
    void testSendVerificationEmail_recipientAndSubject() throws Exception {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendVerificationEmail(
                "jugador@escuelaing.edu.co", "Juan", "Perez",
                "token-activacion-123", UserType.INTERNAL, Role.PLAYER);

        verify(mailSender, times(1)).send(mimeMessage);
        assertEquals("jugador@escuelaing.edu.co",
                mimeMessage.getAllRecipients()[0].toString());
        assertEquals("Verifica tu correo electrónico - Tech Cup DOSW",
                mimeMessage.getSubject());
    }

    @Test
    @DisplayName("El cuerpo del email debe contener el token y el link de verificación")
    void testSendVerificationEmail_contentContainsTokenAndLink() throws Exception {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        String token = "uuid-token-xyz-456";
        emailService.sendVerificationEmail(
                "player@example.com", "Ana", "Garcia",
                token, UserType.EXTERNAL, Role.PLAYER);

        verify(mailSender, times(1)).send(mimeMessage);
        String content = mimeMessage.getContent().toString();
        assertTrue(content.contains(token),
                "El correo debe contener el token de activación");
        assertTrue(content.contains("http://localhost:3000/verify-email"),
                "El correo debe contener la URL de verificación");
        assertTrue(content.contains("?token=" + token),
                "El correo debe incluir el token como query param en el enlace");
    }

    @Test
    @DisplayName("El correo del capitán debe tener el mensaje de rol específico")
    void testSendVerificationEmail_captainRoleMessage() throws Exception {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendVerificationEmail(
                "capitan@escuelaing.edu.co", "Carlos", "Lopez",
                "token-capt-001", UserType.INTERNAL, Role.CAPTAIN);

        verify(mailSender, times(1)).send(mimeMessage);
        String content = mimeMessage.getContent().toString();
        assertTrue(content.contains("capi") || content.contains("Capit"),
                "El correo para capitán debe mencionar su rol");
    }

    @Test
    @DisplayName("No debe lanzar excepción si el servidor SMTP no está disponible")
    void testSendVerificationEmail_doesNotThrowOnMailError() {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("SMTP server unavailable"))
                .when(mailSender).send(any(MimeMessage.class));

        assertDoesNotThrow(() ->
                emailService.sendVerificationEmail(
                        "user@example.com", "Test", "User",
                        "token-err", UserType.INTERNAL, Role.PLAYER));
    }

    @Test
    @DisplayName("El reenvío de verificación debe llamar al mismo flujo de envío")
    void testResendVerificationEmail_sendsEmailCorrectly() throws Exception {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendVerificationEmailResend(
                "player@example.com", "Maria", "Torres",
                "resend-token-789", UserType.INTERNAL, Role.PLAYER);

        verify(mailSender, times(1)).send(mimeMessage);
        assertEquals("player@example.com",
                mimeMessage.getAllRecipients()[0].toString());
        assertEquals("Verifica tu correo electrónico - Tech Cup DOSW",
                mimeMessage.getSubject());
        String content = mimeMessage.getContent().toString();
        assertTrue(content.contains("resend-token-789"));
    }

    @Test
    @DisplayName("El remitente debe ser la dirección configurada")
    void testSendVerificationEmail_fromAddress() throws Exception {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendVerificationEmail(
                "user@example.com", "Pedro", "Ramirez",
                "token-from-check", UserType.INTERNAL, Role.PLAYER);

        verify(mailSender, times(1)).send(mimeMessage);
        String from = mimeMessage.getFrom()[0].toString();
        assertTrue(from.contains("noreply@techcup.edu.co"),
                "El remitente debe ser noreply@techcup.edu.co");
    }
}
