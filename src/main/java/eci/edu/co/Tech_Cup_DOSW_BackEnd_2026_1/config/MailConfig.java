package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Slf4j
@Configuration
public class MailConfig {

    @Value("${spring.mail.host:}")
    private String host;

    @Value("${spring.mail.port:587}")
    private int port;

    @Value("${spring.mail.username:}")
    private String username;

    @Value("${spring.mail.password:}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth:true}")
    private boolean smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable:true}")
    private boolean starttlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required:true}")
    private boolean starttlsRequired;

    @Value("${spring.mail.properties.mail.smtp.connectiontimeout:5000}")
    private int connectionTimeout;

    @Value("${spring.mail.properties.mail.smtp.timeout:5000}")
    private int timeout;

    @Value("${spring.mail.properties.mail.smtp.writetimeout:5000}")
    private int writeTimeout;

    /**
     * Proporciona un JavaMailSender mock solo si se habilita explícitamente.
     * Para SMTP real, dejar app.email.mock-enabled=false.
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "app.email.mock-enabled", havingValue = "true")
    public JavaMailSender mockJavaMailSender() {
        log.warn("Using mock JavaMailSender (app.email.mock-enabled=true)");
        return new JavaMailSenderImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "app.email.mock-enabled", havingValue = "false", matchIfMissing = true)
    public JavaMailSender realJavaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        sender.setHost(host);
        sender.setPort(port);

        if (!isBlank(username)) {
            sender.setUsername(username);
        }
        if (!isBlank(password)) {
            sender.setPassword(password);
        }

        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.auth", Boolean.toString(smtpAuth));
        props.put("mail.smtp.starttls.enable", Boolean.toString(starttlsEnable));
        props.put("mail.smtp.starttls.required", Boolean.toString(starttlsRequired));
        props.put("mail.smtp.connectiontimeout", Integer.toString(connectionTimeout));
        props.put("mail.smtp.timeout", Integer.toString(timeout));
        props.put("mail.smtp.writetimeout", Integer.toString(writeTimeout));

        log.info("Using real JavaMailSender host={} port={} usernameSet={}", host, port, !isBlank(username));
        return sender;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
