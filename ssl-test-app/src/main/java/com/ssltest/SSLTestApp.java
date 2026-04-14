package com.ssltest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Tag(name = "SSL Test API", description = "API para probar configuración HTTPS/SSL y CORS")
public class SSLTestApp {

    public static void main(String[] args) {
        System.out.println("🚀 Iniciando aplicación HTTPS/SSL en puerto 8443...");
        System.out.println("🔄 HTTP disponible en puerto 8080 (redirige automáticamente a HTTPS)");
        SpringApplication.run(SSLTestApp.class, args);
        System.out.println("✅ Aplicación iniciada correctamente");
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🌐 URLs DISPONIBLES:");
        System.out.println("=".repeat(60));
        System.out.println("  📌 Página principal (HTTPS): https://localhost:8443");
        System.out.println("  📌 Página principal (HTTP):  http://localhost:8080");
        System.out.println("  📖 Swagger UI:               https://localhost:8443/swagger-ui.html");
        System.out.println("  📋 OpenAPI JSON:             https://localhost:8443/api-docs");
        System.out.println("=".repeat(60) + "\n");
    }

    @GetMapping("/api/test/ssl")
    @Operation(summary = "Probar conexión HTTPS/SSL", description = "Endpoint para verificar que la conexión HTTPS está funcionando correctamente")
    public String testSSL() {
        return "✅ HTTPS/SSL funcionando correctamente en puerto 8443 - " + java.time.LocalDateTime.now();
    }

    @GetMapping("/api/test/cors")
    @Operation(summary = "Probar configuración CORS", description = "Endpoint para verificar que CORS está habilitado y funcionando")
    public String testCORS() {
        return "✅ CORS habilitado - puedes acceder desde localhost con credenciales";
    }

    @GetMapping("/")
    @Operation(summary = "Página principal", description = "Página de bienvenida con información sobre la aplicación SSL")
    public String home() {
        return "<!DOCTYPE html>" +
                "<html><head><title>🚀 Servidor HTTPS Activo</title><meta charset='utf-8'><meta name='viewport' content='width=device-width, initial-scale=1'></head>"
                +
                "<body style='font-family: Segoe UI, Arial, sans-serif; margin: 0; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh;'>"
                +
                "<div style='max-width: 900px; margin: 0 auto; background: white; padding: 40px; border-radius: 10px; box-shadow: 0 10px 40px rgba(0,0,0,0.2);'>"
                +
                "<h1 style='color: #2E7D32; text-align: center; margin-top: 0;'>🔒 Servidor HTTPS Activo en Puerto 8443</h1>"
                +
                "<p style='text-align: center; color: #666; font-size: 16px;'>Aplicación de prueba para validar configuración HTTPS/SSL y CORS</p>"
                +

                "<hr style='border: none; border-top: 2px solid #EEE; margin: 30px 0;'>" +

                "<h2 style='color: #1976D2; border-bottom: 3px solid #1976D2; padding-bottom: 10px;'>📖 API Endpoints Disponibles</h2>"
                +
                "<div style='background: #F5F5F5; padding: 20px; border-radius: 5px; margin: 20px 0;'>" +
                "<p style='margin: 10px 0;'><strong>✅ Prueba SSL:</strong> <a href='/api/test/ssl' style='color: #1976D2;'>/api/test/ssl</a></p>"
                +
                "<p style='margin: 10px 0;'><strong>✅ Prueba CORS:</strong> <a href='/api/test/cors' style='color: #1976D2;'>/api/test/cors</a></p>"
                +
                "<p style='margin: 10px 0;'><strong>📚 Swagger UI:</strong> <a href='/swagger-ui.html' style='color: #1976D2; font-weight: bold;'>https://localhost:8443/swagger-ui.html</a></p>"
                +
                "</div>" +

                "<h2 style='color: #1976D2; border-bottom: 3px solid #1976D2; padding-bottom: 10px;'>🔍 Cómo Verificar el Certificado SSL</h2>"
                +
                "<ol style='font-size: 15px; line-height: 1.8;'>" +
                "<li><strong>Haz clic en el candado 🔒</strong> en la barra de direcciones (esquina izquierda)</li>" +
                "<li><strong>Selecciona 'Certificado'</strong> o 'Conexión segura' / 'Connection not secure'</li>" +
                "<li><strong>Verifica los detalles correctos:</strong>" +
                "<ul style='margin-top: 10px;'>" +
                "<li><strong>✓ Propietario:</strong> CN=localhost, OU=TechCup, O=DOSW</li>" +
                "<li><strong>✓ Emisor:</strong> CN=localhost (auto-firmado)</li>" +
                "<li><strong>✓ Algoritmo:</strong> SHA384withRSA (RSA 2048-bit)</li>" +
                "<li><strong>✓ Validez:</strong> Sun Mar 29 2026 hasta Wed Mar 26 2036 (10 años)</li>" +
                "<li><strong>✓ Serial:</strong> 360ebe4e30252773</li>" +
                "</ul>" +
                "</li>" +
                "</ol>" +

                "<div style='background: #FFF3CD; border-left: 4px solid #FFC107; padding: 20px; margin: 20px 0; border-radius: 5px;'>"
                +
                "<p style='margin: 0; font-weight: bold; color: #856404;'>⚠️ Importante: 'No seguro' es NORMAL en desarrollo</p>"
                +
                "<p style='margin: 10px 0 0 0; color: #856404;'>" +
                "✓ <strong>La conexión HTTPS está funcionando correctamente</strong><br>" +
                "✓ <strong>El certificado es válido y funcional</strong> (válido hasta 2036)<br>" +
                "ℹ️ Dice \"No seguro\" porque NO está firmado por una Autoridad Certificadora (CA)<br>" +
                "✓ <strong>Esto es CORRECTO y ESPERADO en desarrollo local</strong><br>" +
                "ℹ️ En producción, usarías certificados de una CA como Let's Encrypt o DigiCert<br>" +
                "</p>" +
                "</div>" +

                "<h2 style='color: #1976D2; border-bottom: 3px solid #1976D2; padding-bottom: 10px;'>🌐 Acceso HTTP vs HTTPS</h2>"
                +
                "<div style='background: #E8F5E9; padding: 20px; border-radius: 5px; margin: 20px 0;'>" +
                "<p style='margin: 10px 0;'><strong>HTTP (Puerto 8080):</strong> <a href='http://localhost:8080'>http://localhost:8080</a></p>"
                +
                "<p style='margin: 10px 0; color: #666;'>Se redirige automáticamente a HTTPS. Úsalo para inspeccionar el certificado en el navegador.</p>"
                +
                "<p style='margin: 10px 0;'><strong>HTTPS (Puerto 8443):</strong> <a href='https://localhost:8443' style='font-weight: bold;'>https://localhost:8443</a></p>"
                +
                "<p style='margin: 10px 0; color: #666;'>Conecta directamente sobre HTTPS seguro.</p>" +
                "</div>" +

                "<h2 style='color: #1976D2; border-bottom: 3px solid #1976D2; padding-bottom: 10px;'>📦 Detalles de la Configuración</h2>"
                +
                "<div style='background: #F5F5F5; padding: 20px; border-radius: 5px; margin: 20px 0; font-family: monospace; font-size: 13px;'>"
                +
                "<p style='margin: 5px 0;'><strong>Keystore:</strong> keystore.p12 (PKCS12)</p>" +
                "<p style='margin: 5px 0;'><strong>Alias del certificado:</strong> techcup</p>" +
                "<p style='margin: 5px 0;'><strong>Algoritmo de firma:</strong> SHA384withRSA</p>" +
                "<p style='margin: 5px 0;'><strong>Tamaño de clave:</strong> RSA 2048-bit</p>" +
                "<p style='margin: 5px 0;'><strong>Protocolo TLS:</strong> TLS 1.2 y TLS 1.3</p>" +
                "<p style='margin: 5px 0;'><strong>HTTP/2:</strong> Habilitado</p>" +
                "<p style='margin: 5px 0;'><strong>CORS:</strong> Habilitado para localhost:*</p>" +
                "<p style='margin: 5px 0;'><strong>Swagger UI:</strong> Disponible en /swagger-ui.html</p>" +
                "</div>" +

                "<p style='text-align: center; color: #999; font-size: 12px; margin-top: 30px;'>Aplicación iniciada: "
                + java.time.LocalDateTime.now() + "</p>" +
                "</div>" +
                "</body></html>";
    }
}