package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.EmailDeliveryException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final String INSTITUTIONAL_MAIL_DOMAIN = "@mail.escuelaing.edu.co";
    private static final String INSTITUTIONAL_DOMAIN = "@escuelaing.edu.co";

    private final JavaMailSender mailSender;
    private final ResendEmailClient resendEmailClient;

    @Value("${app.email.from:noreply@techcup.edu.co}")
    private String emailFrom;

    @Value("${app.email.verification-url:http://localhost:3000/verify-email}")
    private String verificationUrl;

    @Value("${app.email.reset-password-url:http://localhost:3000/reset-password}")
    private String resetPasswordUrl;

    @Value("${app.email.smtp.primary-enabled:true}")
    private boolean primarySmtpEnabled;

    @Value("${app.email.smtp.enable-provider-routing:true}")
    private boolean providerRoutingEnabled;

    @Value("${spring.mail.host:}")
    private String primaryHost;

    @Value("${spring.mail.username:}")
    private String primaryUsername;

    @Value("${spring.mail.password:}")
    private String primaryPassword;

    @Value("${spring.mail.properties.mail.smtp.auth:true}")
    private boolean primaryAuthEnabled;

    @Value("${app.email.smtp.microsoft.enabled:false}")
    private boolean microsoftEnabled;

    @Value("${app.email.smtp.microsoft.host:smtp.office365.com}")
    private String microsoftHost;

    @Value("${app.email.smtp.microsoft.port:587}")
    private int microsoftPort;

    @Value("${app.email.smtp.microsoft.username:}")
    private String microsoftUsername;

    @Value("${app.email.smtp.microsoft.password:}")
    private String microsoftPassword;

    @Value("${app.email.smtp.microsoft.from:}")
    private String microsoftFrom;

    @Value("${app.email.smtp.microsoft.auth:true}")
    private boolean microsoftAuthEnabled;

    @Value("${app.email.smtp.microsoft.starttls-enable:true}")
    private boolean microsoftStarttlsEnabled;

    @Value("${app.email.smtp.microsoft.starttls-required:true}")
    private boolean microsoftStarttlsRequired;

    @Value("${app.email.smtp.microsoft.connection-timeout:5000}")
    private int microsoftConnectionTimeout;

    @Value("${app.email.smtp.microsoft.timeout:5000}")
    private int microsoftTimeout;

    @Value("${app.email.smtp.microsoft.write-timeout:5000}")
    private int microsoftWriteTimeout;

    @Value("${app.email.smtp.google.enabled:false}")
    private boolean googleEnabled;

    @Value("${app.email.smtp.google.host:smtp.gmail.com}")
    private String googleHost;

    @Value("${app.email.smtp.google.port:587}")
    private int googlePort;

    @Value("${app.email.smtp.google.username:}")
    private String googleUsername;

    @Value("${app.email.smtp.google.password:}")
    private String googlePassword;

    @Value("${app.email.smtp.google.from:}")
    private String googleFrom;

    @Value("${app.email.smtp.google.auth:true}")
    private boolean googleAuthEnabled;

    @Value("${app.email.smtp.google.starttls-enable:true}")
    private boolean googleStarttlsEnabled;

    @Value("${app.email.smtp.google.starttls-required:true}")
    private boolean googleStarttlsRequired;

    @Value("${app.email.smtp.google.connection-timeout:5000}")
    private int googleConnectionTimeout;

    @Value("${app.email.smtp.google.timeout:5000}")
    private int googleTimeout;

    @Value("${app.email.smtp.google.write-timeout:5000}")
    private int googleWriteTimeout;

    @Value("${app.email.mock-enabled:false}")
    private boolean mockEmailEnabled;

    @SuppressWarnings("null")
    @Override
    public void sendVerificationEmail(String email, String firstName, String lastName, String token,
            UserType userType, Role role) {
        try {
            log.info("Sending verification email to: {}", email);
            String htmlContent = buildVerificationEmailContent(firstName, lastName, token, userType, role, email);
            sendWithProviderFallback(
                    email,
                    "verification",
                    provider -> sendHtmlEmail(
                            provider,
                            email,
                            "Verifica tu correo electrónico - Tech Cup DOSW",
                            htmlContent),
                    () -> resendEmailClient.sendHtmlEmail(
                            email,
                            "Verifica tu correo electrónico - Tech Cup DOSW",
                            htmlContent),
                    true,
                    "No se pudo enviar el correo de verificacion");
        } catch (EmailDeliveryException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error sending verification email to {}: {}", email, e.getMessage(), e);
            throw new EmailDeliveryException("Error inesperado enviando correo de verificacion", e);
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
        String body = String.format(
                "Hola %s %s,\n\n" +
                        "Tu correo ha sido verificado exitosamente. Tu cuenta está lista para usar.\n\n" +
                        "Ya puedes acceder a Tech Cup DOSW 2026 y participar en todas las actividades.\n\n" +
                        "¡Gracias por ser parte de nuestra comunidad!\n\n" +
                        "Saludos,\n" +
                        "Equipo Tech Cup DOSW 2026",
                firstName, lastName);
        try {
            log.info("Sending welcome email to: {}", email);
            sendWithProviderFallback(
                    email,
                    "welcome",
                    provider -> sendPlainTextEmail(
                            provider,
                            email,
                            "¡Bienvenido a Tech Cup DOSW 2026!",
                            body),
                    () -> resendEmailClient.sendPlainTextEmail(
                            email,
                            "¡Bienvenido a Tech Cup DOSW 2026!",
                            body),
                    false,
                    "No se pudo enviar el correo de bienvenida");
        } catch (Exception e) {
            log.error("Error sending welcome email to {}: {}", email, e.getMessage(), e);
            // No lanzar excepción para no bloquear la verificación
        }
    }

    @SuppressWarnings("null")
    @Override
    public void sendPasswordResetEmail(String email, String firstName, String lastName, String token) {
        String htmlContent = buildPasswordResetEmailContent(firstName, lastName, token);
        try {
            log.info("Sending password reset email to: {}", email);
            sendWithProviderFallback(
                    email,
                    "password-reset",
                    provider -> sendHtmlEmail(
                            provider,
                            email,
                            "Recuperacion de contrasena - Tech Cup DOSW",
                            htmlContent),
                    () -> resendEmailClient.sendHtmlEmail(
                            email,
                            "Recuperacion de contrasena - Tech Cup DOSW",
                            htmlContent),
                    false,
                    "No se pudo enviar el correo de recuperacion");
        } catch (Exception e) {
            log.error("Unexpected error sending password reset email to {}: {}", email, e.getMessage(), e);
        }
    }

    private void sendWithProviderFallback(String recipientEmail, String flowName, ProviderSendAction smtpAction,
            ApiSendAction resendAction,
            boolean throwOnFailure, String failureMessage) {
        if (mockEmailEnabled) {
            log.warn("Mock email enabled. Skipping {} email send to {}", flowName, recipientEmail);
            return;
        }

        Exception lastException = null;
        List<String> failedProviders = new ArrayList<>();
        boolean resendEnabled = resendEmailClient.isEnabled();
        boolean resendPreferred = resendEnabled && resendEmailClient.isPreferred();

        if (resendPreferred) {
            try {
                resendAction.send();
                log.info("{} email sent to {} using provider resend-api", flowName, recipientEmail);
                return;
            } catch (Exception e) {
                lastException = e;
                failedProviders.add("resend-api");
                log.warn("{} email failed with provider resend-api for {}: {}",
                        flowName, recipientEmail, e.getMessage());
            }
        }

        List<SmtpProvider> providers = resolveProviders(recipientEmail);

        for (SmtpProvider provider : providers) {
            try {
                smtpAction.send(provider);
                log.info("{} email sent to {} using provider {}", flowName, recipientEmail, provider.name());
                return;
            } catch (MessagingException | MailException e) {
                lastException = e;
                failedProviders.add(provider.name());
                log.warn("{} email failed with provider {} for {}: {}",
                        flowName, provider.name(), recipientEmail, e.getMessage());
            } catch (Exception e) {
                lastException = e;
                failedProviders.add(provider.name());
                log.warn("{} email failed with unexpected error in provider {} for {}: {}",
                        flowName, provider.name(), recipientEmail, e.getMessage());
            }
        }

        if (resendEnabled && !resendPreferred) {
            try {
                resendAction.send();
                log.info("{} email sent to {} using provider resend-api", flowName, recipientEmail);
                return;
            } catch (Exception e) {
                lastException = e;
                failedProviders.add("resend-api");
                log.warn("{} email failed with provider resend-api for {}: {}",
                        flowName, recipientEmail, e.getMessage());
            }
        }

        if (failedProviders.isEmpty()) {
            String message = "No hay proveedores SMTP ni Resend configurados para enviar correos";
            log.error(message);
            if (throwOnFailure) {
                throw new EmailDeliveryException(message);
            }
            return;
        }

        String providersSummary = String.join(", ", failedProviders);
        String smtpGuidance = resolveSmtpGuidance(lastException);
        String finalMessage = failureMessage + ". Proveedores intentados: " + providersSummary;
        if (!isBlank(smtpGuidance)) {
            finalMessage += ". " + smtpGuidance;
        }
        log.error("Failed to send {} email to {}. Providers tried: {}", flowName, recipientEmail, providersSummary,
                lastException);
        if (throwOnFailure) {
            throw new EmailDeliveryException(finalMessage, lastException);
        }
    }

    private String resolveSmtpGuidance(Exception exception) {
        Throwable current = exception;
        while (current != null) {
            String message = current.getMessage();
            if (!isBlank(message)) {
                String normalized = message.toLowerCase();
                if (normalized.contains("api key is invalid")
                        || (normalized.contains("status=401") && normalized.contains("resend"))) {
                    return "Resend rechazo la autenticacion (401). "
                            + "Verifica que RESEND_API_KEY sea valida, vigente y pertenezca al entorno correcto "
                            + "(test/produccion).";
                }
                if (normalized.contains("smtpclientauthentication is disabled for the tenant")) {
                    return "SMTP AUTH esta deshabilitado para el tenant de Microsoft 365. "
                            + "Habilita SMTP AUTH en el tenant y en el mailbox para completar el registro.";
                }
                if (normalized.contains("535 5.7.3") || normalized.contains("authentication unsuccessful")) {
                    return "Autenticacion SMTP rechazada por Microsoft 365 (535 5.7.3). "
                            + "Verifica usuario SMTP, contrasena/app password y que SMTP AUTH este habilitado "
                            + "en tenant y mailbox.";
                }
                if (normalized.contains("you can only send testing emails to your own email address")
                        || normalized.contains("verify a domain at resend.com/domains")) {
                    return "Resend esta en modo de prueba (sandbox) y solo permite enviar al correo del owner. "
                            + "Para enviar a otros destinatarios, verifica un dominio en resend.com/domains "
                            + "y configura RESEND_FROM con ese dominio verificado.";
                }
            }
            current = current.getCause();
        }
        return "";
    }

    private void sendHtmlEmail(SmtpProvider provider, String to, String subject, String htmlContent)
            throws MessagingException {
        MimeMessage mimeMessage = provider.sender().createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setFrom(provider.from());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        provider.sender().send(mimeMessage);
    }

    private void sendPlainTextEmail(SmtpProvider provider, String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(provider.from());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        provider.sender().send(message);
    }

    private List<SmtpProvider> resolveProviders(String recipientEmail) {
        List<SmtpProvider> providers = new ArrayList<>();

        if (providerRoutingEnabled && isInstitutionalRecipient(recipientEmail)) {
            addMicrosoftProvider(providers);
            addGoogleProvider(providers);
        } else if (providerRoutingEnabled) {
            addGoogleProvider(providers);
            addMicrosoftProvider(providers);
        }

        addPrimaryProvider(providers);

        if (!providerRoutingEnabled) {
            addMicrosoftProvider(providers);
            addGoogleProvider(providers);
        }

        return providers;
    }

    private void addPrimaryProvider(List<SmtpProvider> providers) {
        if (!isPrimaryProviderConfigured()) {
            return;
        }

        providers.add(new SmtpProvider(
                "primary",
                mailSender,
                resolveFromAddress(emailFrom, primaryUsername)));
    }

    private void addMicrosoftProvider(List<SmtpProvider> providers) {
        if (!isProviderConfigured(microsoftEnabled, microsoftHost, microsoftAuthEnabled,
                microsoftUsername, microsoftPassword)) {
            return;
        }

        providers.add(new SmtpProvider(
                "microsoft",
                buildProviderSender(
                        microsoftHost,
                        microsoftPort,
                        microsoftUsername,
                        microsoftPassword,
                        microsoftAuthEnabled,
                        microsoftStarttlsEnabled,
                        microsoftStarttlsRequired,
                        microsoftConnectionTimeout,
                        microsoftTimeout,
                        microsoftWriteTimeout),
                resolveFromAddress(microsoftFrom, microsoftUsername)));
    }

    private void addGoogleProvider(List<SmtpProvider> providers) {
        if (!isProviderConfigured(googleEnabled, googleHost, googleAuthEnabled,
                googleUsername, googlePassword)) {
            return;
        }

        providers.add(new SmtpProvider(
                "google",
                buildProviderSender(
                        googleHost,
                        googlePort,
                        googleUsername,
                        googlePassword,
                        googleAuthEnabled,
                        googleStarttlsEnabled,
                        googleStarttlsRequired,
                        googleConnectionTimeout,
                        googleTimeout,
                        googleWriteTimeout),
                resolveFromAddress(googleFrom, googleUsername)));
    }

    private JavaMailSender buildProviderSender(String host, int port, String username, String password,
            boolean authEnabled, boolean starttlsEnabled, boolean starttlsRequired,
            int connectionTimeout, int timeout, int writeTimeout) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(password);

        Properties properties = sender.getJavaMailProperties();
        properties.put("mail.smtp.auth", Boolean.toString(authEnabled));
        properties.put("mail.smtp.starttls.enable", Boolean.toString(starttlsEnabled));
        properties.put("mail.smtp.starttls.required", Boolean.toString(starttlsRequired));
        properties.put("mail.smtp.connectiontimeout", Integer.toString(connectionTimeout));
        properties.put("mail.smtp.timeout", Integer.toString(timeout));
        properties.put("mail.smtp.writetimeout", Integer.toString(writeTimeout));

        return sender;
    }

    private boolean isPrimaryProviderConfigured() {
        if (!primarySmtpEnabled || isBlank(primaryHost)) {
            return false;
        }
        if (!primaryAuthEnabled) {
            return true;
        }
        return !isBlank(primaryUsername) && !isBlank(primaryPassword);
    }

    private boolean isProviderConfigured(boolean enabled, String host, boolean authEnabled,
            String username, String password) {
        if (!enabled || isBlank(host)) {
            return false;
        }
        if (!authEnabled) {
            return true;
        }
        return !isBlank(username) && !isBlank(password);
    }

    private String resolveFromAddress(String configuredFrom, String fallbackUsername) {
        if (!isBlank(configuredFrom)) {
            return configuredFrom;
        }
        if (!isBlank(fallbackUsername)) {
            return fallbackUsername;
        }
        return emailFrom;
    }

    private boolean isInstitutionalRecipient(String email) {
        if (isBlank(email)) {
            return false;
        }
        String normalizedEmail = email.trim().toLowerCase();
        return normalizedEmail.endsWith(INSTITUTIONAL_MAIL_DOMAIN)
                || normalizedEmail.endsWith(INSTITUTIONAL_DOMAIN);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    @FunctionalInterface
    private interface ProviderSendAction {
        void send(SmtpProvider provider) throws Exception;
    }

    @FunctionalInterface
    private interface ApiSendAction {
        void send() throws Exception;
    }

    private record SmtpProvider(String name, JavaMailSender sender, String from) {
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
