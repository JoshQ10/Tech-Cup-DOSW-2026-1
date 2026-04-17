package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.LoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestValidatorTest {

    private LoginRequestValidator validator;

    @BeforeEach
    void setUp() {
        validator = new LoginRequestValidator();
    }

    @Test
    void should_pass_validation_when_email_and_password_are_valid() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("password123");

        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    void should_pass_validation_when_username_and_password_are_valid() {
        LoginRequest request = new LoginRequest();
        request.setUsername("user123");
        request.setPassword("password123");

        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    void should_throw_exception_when_identifier_is_missing() {
        LoginRequest request = new LoginRequest();
        request.setPassword("password123");

        assertThrows(ValidationException.class,
                () -> validator.validate(request));
    }

    @Test
    void should_throw_exception_when_password_is_missing() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");

        assertThrows(ValidationException.class,
                () -> validator.validate(request));
    }

    @Test
    void should_throw_exception_when_identifier_and_password_are_missing() {
        LoginRequest request = new LoginRequest();

        ValidationException exception =
                assertThrows(ValidationException.class,
                        () -> validator.validate(request));

        assertEquals(
                "Errores de validacion en el login",
                exception.getMessage()
        );
    }
}
