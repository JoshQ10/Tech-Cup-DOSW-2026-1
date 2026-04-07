package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

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

    @SuppressWarnings("null")
    @Override
    public void sendVerificationEmail(String email, String firstName, String lastName, String token,
            UserType userType) {
        try {
            log.info("Sending verification email to: {}", email);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom(emailFrom);
            helper.setTo(email);
            helper.setSubject("Verifica tu correo electrónico - Tech Cup DOSW");

            String htmlContent = buildVerificationEmailContent(firstName, lastName, token, userType, email);
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
            UserType userType) {
        log.info("Resending verification email to: {}", email);
        // Usar el mismo contenido que sendVerificationEmail
        sendVerificationEmail(email, firstName, lastName, token, userType);
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

    private String buildVerificationEmailContent(String firstName, String lastName, String token, UserType userType,
            String email) {
        String verificationLink = String.format("%s?token=%s", verificationUrl, token);

        String userTypeLabel = userType == UserType.INTERNAL ? "Estudiante" : "Visitante";
        String welcomeMessage = userType == UserType.INTERNAL
                ? "¡Bienvenido a Tech Cup DOSW! Como estudiante de la Escuela Colombiana de Ingeniería Julio Garavito, estamos emocionados de tenerte en nuestro equipo."
                : "¡Bienvenido a Tech Cup DOSW! Nos complace contar con tu participación como visitante.";

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
                                        <p><strong>Correo Registrado:</strong> %s</p>
                                    </div>
                                    <div class="verification-section">
                                        <p style="margin-top: 0;"><strong>Para completar tu registro, verifica tu correo haciendo clic en el siguiente botón:</strong></p>
                                        <a href="%s" class="button">✓ Verificar Correo Electrónico</a>
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
                welcomeMessage,
                userTypeLabel,
                email,
                verificationLink,
                verificationLink);
    }
}
