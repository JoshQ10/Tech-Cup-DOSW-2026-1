package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Slf4j
@Configuration
public class MailConfig {

    /**
     * Proporciona un JavaMailSender mock cuando no hay configuración de mail.
     * Útil para desarrollo cuando no se tiene SMTP configurado.
     */
    @Bean
    @ConditionalOnMissingBean
    public JavaMailSender javaMailSender() {
        log.info("Using mock JavaMailSender for development");
        return new JavaMailSenderImpl();
    }
}
