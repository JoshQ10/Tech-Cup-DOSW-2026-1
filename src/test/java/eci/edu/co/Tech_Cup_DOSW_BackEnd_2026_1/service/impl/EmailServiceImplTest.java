package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.EmailServiceImpl;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailServiceImpl Unit Tests")
class EmailServiceImplTest {

        @Mock
        private JavaMailSender mailSender;

        private EmailServiceImpl emailService;

        private static final String TEST_EMAIL = "test@example.com";
        private static final String TEST_FIRST_NAME = "John";
        private static final String TEST_LAST_NAME = "Doe";
        private static final String TEST_TOKEN = "verification-token-12345";
        private static final String EMAIL_FROM = "noreply@techcup.edu.co";
        private static final String VERIFICATION_URL = "http://localhost:3000/verify-email";
        private static final String RESET_PASSWORD_URL = "http://localhost:3000/reset-password";

        @BeforeEach
        void setUp() {
                emailService = new EmailServiceImpl(mailSender);
                ReflectionTestUtils.setField(emailService, "emailFrom", EMAIL_FROM);
                ReflectionTestUtils.setField(emailService, "verificationUrl", VERIFICATION_URL);
                ReflectionTestUtils.setField(emailService, "resetPasswordUrl", RESET_PASSWORD_URL);
        }

        @Test
        @DisplayName("Should send verification email successfully with INTERNAL user and PLAYER role")
        void testSendVerificationEmailSuccess() throws Exception {
                MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties()));
                when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

                emailService.sendVerificationEmail(TEST_EMAIL, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_TOKEN,
                                UserType.INTERNAL, Role.PLAYER);

                verify(mailSender).createMimeMessage();
                verify(mailSender).send(any(MimeMessage.class));
        }

        @Test
        @DisplayName("Should handle exception gracefully when sending welcome email fails")
        void testSendWelcomeEmailWithException() {
                doThrow(new RuntimeException("Mail server connection failed"))
                                .when(mailSender).send(any(SimpleMailMessage.class));

                emailService.sendWelcomeEmail(TEST_EMAIL, TEST_FIRST_NAME, TEST_LAST_NAME);

                verify(mailSender).send(any(SimpleMailMessage.class));
        }

        @Test
        @DisplayName("Should send verification email resend successfully")
        void testSendVerificationEmailResend() throws Exception {
                MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties()));
                when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

                emailService.sendVerificationEmailResend(TEST_EMAIL, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_TOKEN,
                                UserType.INTERNAL, Role.CAPTAIN);

                verify(mailSender).createMimeMessage();
                verify(mailSender).send(any(MimeMessage.class));
        }

        @Test
        @DisplayName("Should send password reset email successfully")
        void testSendPasswordResetEmailSuccess() throws Exception {
                MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties()));
                when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

                emailService.sendPasswordResetEmail(TEST_EMAIL, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_TOKEN);

                verify(mailSender).createMimeMessage();
                verify(mailSender).send(any(MimeMessage.class));
        }

        @Test
        @DisplayName("Should handle MessagingException when creating MIME message fails")
        void testSendVerificationEmailWithMessagingException() throws Exception {
                MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties()));
                when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
                doThrow(new RuntimeException("SMTP connection failed"))
                                .when(mailSender).send(any(MimeMessage.class));

                emailService.sendVerificationEmail(TEST_EMAIL, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_TOKEN,
                                UserType.EXTERNAL, Role.ADMINISTRATOR);

                verify(mailSender).send(any(MimeMessage.class));
        }

        @Test
        @DisplayName("Should send welcome email successfully")
        void testSendWelcomeEmailSuccess() {
                emailService.sendWelcomeEmail(TEST_EMAIL, TEST_FIRST_NAME, TEST_LAST_NAME);

                verify(mailSender).send(any(SimpleMailMessage.class));
        }

        @Test
        @DisplayName("Should handle MessagingException when sending password reset email fails")
        void testSendPasswordResetEmailWithMessagingException() throws Exception {
                MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties()));
                when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
                doThrow(new RuntimeException("Failed to send email"))
                                .when(mailSender).send(any(MimeMessage.class));

                emailService.sendPasswordResetEmail(TEST_EMAIL, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_TOKEN);

                verify(mailSender).send(any(MimeMessage.class));
        }
}
