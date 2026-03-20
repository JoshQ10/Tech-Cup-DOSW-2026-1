package edu.dosw.proyecto.Tech_Cup_Football_2026_1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public void enviarEmail(String destinatario, String asunto, String cuerpo) {
        try {
            if (mailSender != null) {
                SimpleMailMessage mensaje = new SimpleMailMessage();
                mensaje.setFrom("noreply@techcup.com");
                mensaje.setTo(destinatario);
                mensaje.setSubject(asunto);
                mensaje.setText(cuerpo);
                mailSender.send(mensaje);
            } else {
                // En desarrollo sin configuración de SMTP
                System.out.println("Email enviado a: " + destinatario);
                System.out.println("Asunto: " + asunto);
                System.out.println("Contenido: " + cuerpo);
            }
        } catch (Exception e) {
            System.err.println("Error al enviar email: " + e.getMessage());
        }
    }
}

