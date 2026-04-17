package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

/**
 * Test de integración de correo real.
 *
 * Para ejecutar, define las variables de entorno antes de correr el test:
 *
 *   MAIL_USERNAME   → tu cuenta Gmail (ej: tucuenta@gmail.com)
 *   MAIL_PASSWORD   → contraseña de aplicación de Google (16 caracteres)
 *   TEST_EMAIL_TO   → dirección a la que quieres recibir el correo de prueba
 *
 * Crea una contraseña de aplicación en:
 *   https://myaccount.google.com/apppasswords
 *
 * En PowerShell, antes de ejecutar:
 *   $env:MAIL_USERNAME="tucuenta@gmail.com"
 *   $env:MAIL_PASSWORD="xxxx xxxx xxxx xxxx"
 *   $env:TEST_EMAIL_TO="destino@example.com"
 *
 * Luego corre solo este test:
 *   .\mvnw.cmd test -Dtest=EmailActivationRealTest -pl .
 */
@DisplayName("Envío real de correo de activación")
class EmailActivationRealTest {

    private static final String MAIL_USERNAME_ENV = "MAIL_USERNAME";
    private static final String MAIL_PASSWORD_ENV = "MAIL_PASSWORD";
    private static final String TEST_EMAIL_TO_ENV = "TEST_EMAIL_TO";

    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        String host = System.getenv().getOrDefault("MAIL_HOST", "smtp.gmail.com");
        int port = Integer.parseInt(System.getenv().getOrDefault("MAIL_PORT", "587"));
        String username = System.getenv(MAIL_USERNAME_ENV);
        String password = System.getenv(MAIL_PASSWORD_ENV);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");

        emailService = new EmailServiceImpl(mailSender);
        ReflectionTestUtils.setField(emailService, "emailFrom", username);
        ReflectionTestUtils.setField(emailService, "verificationUrl",
                "http://localhost:3000/verify-email");
        ReflectionTestUtils.setField(emailService, "resetPasswordUrl",
                "http://localhost:3000/reset-password");
    }

    @Test
    @DisplayName("Envía correo de activación real a jugador INTERNAL")
    @EnabledIfEnvironmentVariable(named = MAIL_USERNAME_ENV, matches = ".+")
    @EnabledIfEnvironmentVariable(named = MAIL_PASSWORD_ENV, matches = ".+")
    @EnabledIfEnvironmentVariable(named = TEST_EMAIL_TO_ENV, matches = ".+")
    void testEnviaCorreoActivacionReal_Jugador() {
        String destino = System.getenv(TEST_EMAIL_TO_ENV);

        System.out.println(">>> Enviando correo de activación a: " + destino);

        emailService.sendVerificationEmail(
                destino,
                "Juan",
                "Prueba",
                "token-test-real-" + System.currentTimeMillis(),
                UserType.INTERNAL,
                Role.PLAYER);

        System.out.println(">>> Correo enviado. Revisa la bandeja de entrada de: " + destino);
    }

    @Test
    @DisplayName("Envía correo de activación real a capitán INTERNAL")
    @EnabledIfEnvironmentVariable(named = MAIL_USERNAME_ENV, matches = ".+")
    @EnabledIfEnvironmentVariable(named = MAIL_PASSWORD_ENV, matches = ".+")
    @EnabledIfEnvironmentVariable(named = TEST_EMAIL_TO_ENV, matches = ".+")
    void testEnviaCorreoActivacionReal_Capitan() {
        String destino = System.getenv(TEST_EMAIL_TO_ENV);

        System.out.println(">>> Enviando correo de activación (CAPITÁN) a: " + destino);

        emailService.sendVerificationEmail(
                destino,
                "Carlos",
                "Capitan",
                "token-capt-real-" + System.currentTimeMillis(),
                UserType.INTERNAL,
                Role.CAPTAIN);

        System.out.println(">>> Correo enviado. Revisa la bandeja de entrada de: " + destino);
    }

    @Test
    @DisplayName("Envía correo de activación real a jugador EXTERNAL (visitante)")
    @EnabledIfEnvironmentVariable(named = MAIL_USERNAME_ENV, matches = ".+")
    @EnabledIfEnvironmentVariable(named = MAIL_PASSWORD_ENV, matches = ".+")
    @EnabledIfEnvironmentVariable(named = TEST_EMAIL_TO_ENV, matches = ".+")
    void testEnviaCorreoActivacionReal_Visitante() {
        String destino = System.getenv(TEST_EMAIL_TO_ENV);

        System.out.println(">>> Enviando correo de activación (VISITANTE) a: " + destino);

        emailService.sendVerificationEmail(
                destino,
                "Maria",
                "Visitante",
                "token-visit-real-" + System.currentTimeMillis(),
                UserType.EXTERNAL,
                Role.PLAYER);

        System.out.println(">>> Correo enviado. Revisa la bandeja de entrada de: " + destino);
    }
}
