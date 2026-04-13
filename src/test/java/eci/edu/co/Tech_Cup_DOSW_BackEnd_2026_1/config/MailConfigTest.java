package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.config;

import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

class MailConfigTest {

    @Test
    void javaMailSender_isExecuted() {
        MailConfig config = new MailConfig();

        JavaMailSender sender = config.javaMailSender();

        assertNotNull(sender);
    }
}