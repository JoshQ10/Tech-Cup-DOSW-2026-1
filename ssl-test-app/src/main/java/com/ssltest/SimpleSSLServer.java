package com.ssltest;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class SimpleSSLServer {
    public static void main(String[] args) throws Exception {
        System.out.println("🚀 Iniciando servidor HTTPS simple en puerto 8443...");

        // Configurar propiedades del sistema para el keystore
        System.setProperty("javax.net.ssl.keyStore", "src/main/resources/keystore.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");

        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(8443);

        System.out.println("✅ Servidor HTTPS iniciado correctamente");
        System.out.println("🌐 Accede a: https://localhost:8443");
        System.out.println("🔒 El certificado SSL está disponible para inspección");

        while (true) {
            SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
            System.out.println("🔗 Nueva conexión desde: " + sslSocket.getInetAddress());

            // Responder con HTML simple
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n" +
                    "<!DOCTYPE html>" +
                    "<html><head><title>🚀 Servidor HTTPS Activo</title></head>" +
                    "<body style='font-family: Arial, sans-serif; margin: 40px;'>" +
                    "<h1 style='color: #2E7D32;'>🚀 Servidor HTTPS activo en puerto 8443</h1>" +
                    "<h2>🔒 Certificado SSL Disponible</h2>" +
                    "<p>Haz clic en el candado 🔒 en la barra de direcciones para ver el certificado SSL.</p>" +
                    "<div style='background: #E8F5E8; border: 1px solid #4CAF50; padding: 15px; margin: 20px 0; border-radius: 5px;'>"
                    +
                    "<strong>✅ Éxito:</strong> La conexión HTTPS está funcionando correctamente." +
                    "</div>" +
                    "<p>Conexión establecida: " + java.time.LocalDateTime.now() + "</p>" +
                    "</body></html>";

            OutputStream outputStream = sslSocket.getOutputStream();
            outputStream.write(response.getBytes());
            outputStream.flush();
            sslSocket.close();
        }
    }
}