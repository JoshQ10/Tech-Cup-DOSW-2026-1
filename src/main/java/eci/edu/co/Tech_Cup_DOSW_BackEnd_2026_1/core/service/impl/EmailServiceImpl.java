package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.from:noreply@techcup.edu.co}")
    private String emailFrom;

    @Value("${app.email.verification-url:http://localhost:3000/verify-email}")
    private String verificationUrl;

    @Value("${app.email.reset-password-url:http://localhost:3000/reset-password}")
    private String resetPasswordUrl;

    @SuppressWarnings("null")
    @Override
    public void sendVerificationEmail(String email, String firstName, String lastName, String token,
            UserType userType, Role role) {
        try {
            log.info("Sending verification email to: {}", email);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom(emailFrom);
            helper.setTo(email);
            helper.setSubject("Verifica tu correo electrónico - Tech Cup DOSW");

            String htmlContent = buildVerificationEmailContent(firstName, lastName, token, userType, role, email);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("Verification email sent successfully to: {}", email);

        } catch (MessagingException e) {
            log.error("Error sending verification email to {}: {}", email, e.getMessage(), e);
            // No lanzar excepción para no bloquear el registro
        } catch (Exception e) {
            log.error("Unexpected error sending verification email to {}: {}", email, e.getMessage(), e);
        }
    }

    @Override
    public void sendVerificationEmailResend(String email, String firstName, String lastName, String token,
            UserType userType, Role role) {
        log.info("Resending verification email to: {}", email);
        // Usar el mismo contenido que sendVerificationEmail
        sendVerificationEmail(email, firstName, lastName, token, userType, role);
    }

    @Override
    public void sendWelcomeEmail(String email, String firstName, String lastName) {
        try {
            log.info("Sending welcome email to: {}", email);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(email);
            message.setSubject("¡Bienvenido a Tech Cup DOSW 2026!");
            message.setText(String.format(
                    "Hola %s %s,\n\n" +
                            "Tu correo ha sido verificado exitosamente. Tu cuenta está lista para usar.\n\n" +
                            "Ya puedes acceder a Tech Cup DOSW 2026 y participar en todas las actividades.\n\n" +
                            "¡Gracias por ser parte de nuestra comunidad!\n\n" +
                            "Saludos,\n" +
                            "Equipo Tech Cup DOSW 2026",
                    firstName, lastName));

            mailSender.send(message);
            log.info("Welcome email sent successfully to: {}", email);

        } catch (Exception e) {
            log.error("Error sending welcome email to {}: {}", email, e.getMessage(), e);
            // No lanzar excepción para no bloquear la verificación
        }
    }

    @SuppressWarnings("null")
    @Override
    public void sendPasswordResetEmail(String email, String firstName, String lastName, String token) {
        try {
            log.info("Sending password reset email to: {}", email);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom(emailFrom);
            helper.setTo(email);
            helper.setSubject("Recuperacion de contrasena - Tech Cup DOSW");

            String htmlContent = buildPasswordResetEmailContent(firstName, lastName, token);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("Password reset email sent successfully to: {}", email);

        } catch (MessagingException e) {
            log.error("Error sending password reset email to {}: {}", email, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error sending password reset email to {}: {}", email, e.getMessage(), e);
        }
    }

    private String buildVerificationEmailContent(String firstName, String lastName, String token, UserType userType,
            Role role, String email) {
        String verificationLink = String.format("%s?token=%s", verificationUrl, token);

        String userTypeLabel = userType == UserType.INTERNAL ? "Estudiante" : "Visitante";
        String roleLabel = role != null ? role.name() : "N/A";

        String roleSpecificMessage;
        if (role == Role.CAPTAIN) {
            roleSpecificMessage = "Ahora eres capi, guia a tu equipo.";
        } else if (role == Role.PLAYER) {
            roleSpecificMessage = "Gracias por crear tu cuenta, dale aqui para confirmar.";
        } else {
            roleSpecificMessage = userType == UserType.INTERNAL
                    ? "¡Bienvenido a Tech Cup DOSW! Estamos emocionados de tenerte en nuestro equipo."
                    : "¡Bienvenido a Tech Cup DOSW! Nos complace contar con tu participacion.";
        }

        return String.format(
                """
                        <!DOCTYPE html>
                        <html lang="es">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <style>
                                body {
                                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                    line-height: 1.6;
                                    color: #333;
                                    background-color: #f5f5f5;
                                    margin: 0;
                                    padding: 0;
                                }
                                .container {
                                    max-width: 600px;
                                    margin: 20px auto;
                                    background-color: #ffffff;
                                    padding: 0;
                                    border-radius: 8px;
                                    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                                    overflow: hidden;
                                }
                                .header {
                                    background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                                    color: white;
                                    padding: 30px;
                                    text-align: center;
                                }
                                .header h1 {
                                    margin: 0;
                                    font-size: 24px;
                                }
                                .content {
                                    padding: 30px;
                                }
                                .greeting {
                                    font-size: 16px;
                                    margin-bottom: 20px;
                                }
                                .message {
                                    background-color: #f9f9f9;
                                    padding: 15px;
                                    border-left: 4px solid #667eea;
                                    margin: 20px 0;
                                    border-radius: 4px;
                                }
                                .verification-section {
                                    text-align: center;
                                    margin: 30px 0;
                                    padding: 20px;
                                    background-color: #f0f4ff;
                                    border-radius: 6px;
                                }
                                .button {
                                    display: inline-block;
                                    background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                                    color: white;
                                    padding: 12px 30px;
                                    text-decoration: none;
                                    border-radius: 4px;
                                    font-weight: bold;
                                    margin: 10px 0;
                                    transition: opacity 0.3s;
                                }
                                .button:hover {
                                    opacity: 0.9;
                                }
                                .button-text {
                                    word-break: break-all;
                                    font-size: 12px;
                                    color: #666;
                                    margin-top: 10px;
                                }
                                .footer {
                                    background-color: #f5f5f5;
                                    padding: 20px;
                                    text-align: center;
                                    font-size: 12px;
                                    color: #666;
                                    border-top: 1px solid #ddd;
                                }
                                .user-info {
                                    background-color: #e8f4f8;
                                    padding: 15px;
                                    border-radius: 4px;
                                    margin: 15px 0;
                                }
                                .user-info p {
                                    margin: 5px 0;
                                }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <h1>🎯 Tech Cup DOSW 2026</h1>
                                    <p>Verificación de Correo Electrónico</p>
                                </div>
                                <div class="content">
                                    <div class="greeting">
                                        <strong>Hola %s %s,</strong>
                                    </div>
                                    <div class="message">
                                        %s
                                    </div>
                                    <div class="user-info">
                                        <p><strong>Tipo de Usuario:</strong> %s</p>
                                        <p><strong>Rol:</strong> %s</p>
                                        <p><strong>Correo Registrado:</strong> %s</p>
                                    </div>
                                    <div class="verification-section">
                                        <p style="margin-top: 0;"><strong>Para completar tu registro, verifica tu correo haciendo clic en el siguiente botón:</strong></p>
                                        <a href="%s" class="button">Activar mi Cuenta</a>
                                        <div class="button-text">
                                            <strong>O copia este enlace:</strong><br>
                                            %s
                                        </div>
                                    </div>
                                    <p style="color: #666; font-size: 14px; margin-top: 20px;">
                                        <strong>Nota:</strong> Este enlace es válido por 24 horas. Si no creaste esta cuenta, puedes ignorar este correo.
                                    </p>
                                </div>
                                <div class="footer">
                                    <p style="margin: 0;">
                                        © 2026 Tech Cup DOSW - Escuela Colombiana de Ingeniería Julio Garavito<br>
                                        Este es un correo automático, por favor no respondas.
                                    </p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """,
                firstName, lastName,
                roleSpecificMessage,
                userTypeLabel,
                roleLabel,
                email,
                verificationLink,
                verificationLink);
    }

    private String buildPasswordResetEmailContent(String firstName, String lastName, String token) {
        String resetLink = String.format("%s?token=%s", resetPasswordUrl, token);

        return String.format(
                """
                        <!DOCTYPE html>
                        <html lang="es">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <style>
                                body {
                                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                    line-height: 1.6;
                                    color: #333;
                                    background-color: #f5f5f5;
                                    margin: 0;
                                    padding: 0;
                                }
                                .container {
                                    max-width: 600px;
                                    margin: 20px auto;
                                    background-color: #ffffff;
                                    border-radius: 8px;
                                    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                                    overflow: hidden;
                                }
                                .header {
                                    background: linear-gradient(135deg, #0f766e 0%%, #14b8a6 100%%);
                                    color: white;
                                    padding: 28px;
                                    text-align: center;
                                }
                                .content {
                                    padding: 28px;
                                }
                                .notice {
                                    background-color: #ecfeff;
                                    border-left: 4px solid #14b8a6;
                                    padding: 14px;
                                    border-radius: 4px;
                                    margin: 18px 0;
                                }
                                .button {
                                    display: inline-block;
                                    background: linear-gradient(135deg, #0f766e 0%%, #14b8a6 100%%);
                                    color: white;
                                    padding: 12px 30px;
                                    text-decoration: none;
                                    border-radius: 4px;
                                    font-weight: bold;
                                }
                                .button-wrap {
                                    text-align: center;
                                    margin: 24px 0;
                                }
                                .small {
                                    color: #666;
                                    font-size: 12px;
                                    word-break: break-all;
                                }
                                .footer {
                                    background-color: #f5f5f5;
                                    padding: 18px;
                                    text-align: center;
                                    font-size: 12px;
                                    color: #666;
                                }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <h1>Tech Cup DOSW 2026</h1>
                                    <p>Recuperacion de contrasena</p>
                                </div>
                                <div class="content">
                                    <p><strong>Hola %s %s,</strong></p>
                                    <div class="notice">
                                        Olvidaste tu contrasena, no te preocupes, te enviaremos un correo para restablecerla.
                                    </div>
                                    <p>Haz clic en el siguiente boton para crear una nueva contrasena:</p>
                                    <div class="button-wrap">
                                        <a href="%s" class="button">Restablecer mi contrasena</a>
                                    </div>
                                    <p class="small"><strong>O copia este enlace:</strong><br>%s</p>
                                    <p style="color:#666; font-size:13px;">Este enlace es valido por 1 hora. Si no solicitaste este cambio, ignora este mensaje.</p>
                                </div>
                                <div class="footer">
                                    © 2026 Tech Cup DOSW - Correo automatico
                                </div>
                            </div>
                        </body>
                        </html>
                        """,
                firstName,
                lastName,
                resetLink,
                resetLink);
    }
}
