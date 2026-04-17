package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.EmailDeliveryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResendEmailClient {

    private final ObjectMapper objectMapper;

    @Value("${app.email.resend.enabled:false}")
    private boolean enabled;

    @Value("${app.email.resend.prefer-api:false}")
    private boolean preferApi;

    @Value("${app.email.resend.base-url:https://api.resend.com}")
    private String baseUrl;

    @Value("${app.email.resend.api-key:}")
    private String apiKey;

    @Value("${app.email.resend.from:}")
    private String fromAddress;

    @Value("${app.email.resend.fallback-from:onboarding@resend.dev}")
    private String fallbackFromAddress;

    @Value("${app.email.resend.reply-to:}")
    private String replyToAddress;

    @Value("${app.email.resend.connect-timeout-ms:5000}")
    private int connectTimeoutMs;

    @Value("${app.email.resend.request-timeout-ms:10000}")
    private int requestTimeoutMs;

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isPreferred() {
        return enabled && preferApi;
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        sendEmail(to, subject, htmlContent, null);
    }

    public void sendPlainTextEmail(String to, String subject, String body) {
        sendEmail(to, subject, null, body);
    }

    private void sendEmail(String to, String subject, String htmlContent, String textContent) {
        if (!enabled) {
            throw new EmailDeliveryException("Resend API esta deshabilitada");
        }
        if (isBlank(apiKey)) {
            throw new EmailDeliveryException("RESEND_API_KEY no esta configurada");
        }

        String primaryFrom = resolvePrimaryFrom();
        if (isBlank(primaryFrom)) {
            throw new EmailDeliveryException("RESEND_FROM no esta configurado y no hay fallback disponible");
        }

        try {
            sendEmailRequest(to, subject, htmlContent, textContent, primaryFrom, null);
            return;
        } catch (EmailDeliveryException e) {
            if (!shouldRetryWithFallbackSender(e, primaryFrom)) {
                throw e;
            }
        }

        String fallbackFrom = fallbackFromAddress.trim();
        String fallbackReplyTo = resolveReplyToForFallback(primaryFrom);

        log.warn("Resend rechazo el remitente {}. Reintentando con fallback sender {}", primaryFrom, fallbackFrom);
        sendEmailRequest(to, subject, htmlContent, textContent, fallbackFrom, fallbackReplyTo);
    }

    private void sendEmailRequest(String to, String subject, String htmlContent, String textContent,
            String senderFrom, String replyTo) {

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("from", senderFrom);
        payload.put("to", List.of(to));
        payload.put("subject", subject);
        if (!isBlank(htmlContent)) {
            payload.put("html", htmlContent);
        }
        if (!isBlank(textContent)) {
            payload.put("text", textContent);
        }
        if (!isBlank(replyTo)) {
            payload.put("reply_to", replyTo);
        }

        String body;
        try {
            body = objectMapper.writeValueAsString(payload);
        } catch (IOException e) {
            throw new EmailDeliveryException("No se pudo serializar la solicitud para Resend API", e);
        }

        String targetUrl = normalizeBaseUrl(baseUrl) + "/emails";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(targetUrl))
                .timeout(Duration.ofMillis(Math.max(requestTimeoutMs, 1000)))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(Math.max(connectTimeoutMs, 1000)))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                String apiMessage = String.format(
                        "Resend API rechazo el envio (status=%d). body=%s",
                        response.statusCode(),
                        trimBody(response.body()));
                throw new EmailDeliveryException(apiMessage);
            }
            log.debug("Email enviado con Resend API a {}", to);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new EmailDeliveryException("Interrupcion durante envio a Resend API", e);
        } catch (IOException e) {
            throw new EmailDeliveryException("Error de red al enviar con Resend API", e);
        }
    }

    private String resolvePrimaryFrom() {
        if (!isBlank(fromAddress)) {
            return fromAddress.trim();
        }
        if (!isBlank(fallbackFromAddress)) {
            return fallbackFromAddress.trim();
        }
        return "";
    }

    private boolean shouldRetryWithFallbackSender(EmailDeliveryException ex, String primaryFrom) {
        if (isBlank(fallbackFromAddress)) {
            return false;
        }

        String fallback = fallbackFromAddress.trim();
        if (fallback.equalsIgnoreCase(primaryFrom)) {
            return false;
        }

        String message = ex.getMessage();
        if (isBlank(message)) {
            return false;
        }

        String normalized = message.toLowerCase();
        boolean isDomainNotVerified = normalized.contains("domain is not verified")
                || normalized.contains("validation_error");
        boolean isForbidden = normalized.contains("status=403");
        return isDomainNotVerified || isForbidden;
    }

    private String resolveReplyToForFallback(String primaryFrom) {
        if (!isBlank(replyToAddress)) {
            return replyToAddress.trim();
        }
        if (!isBlank(fromAddress) && !fromAddress.trim().equalsIgnoreCase(primaryFrom)) {
            return fromAddress.trim();
        }
        return "";
    }

    private String normalizeBaseUrl(String base) {
        if (isBlank(base)) {
            return "https://api.resend.com";
        }
        return base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
    }

    private String trimBody(String body) {
        if (body == null) {
            return "";
        }
        String normalized = body.replaceAll("\\s+", " ").trim();
        return normalized.length() <= 300 ? normalized : normalized.substring(0, 300) + "...";
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
