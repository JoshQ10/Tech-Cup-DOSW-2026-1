package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Test configuration for email services.
 * Provides a mock JavaMailSender bean for test profiles.
 */
@TestConfiguration
@Profile("test")
public class TestMailConfiguration {

    @Bean
    @Primary
    public JavaMailSender testJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(1025);
        return mailSender;
    }
}
