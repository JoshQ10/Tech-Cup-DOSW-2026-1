package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("RegisterRequestValidator Tests")
class RegisterRequestValidatorTest {

    private final RegisterRequestValidator validator = new RegisterRequestValidator();

    @Test
    @DisplayName("Should allow INTERNAL user with non-institutional email")
    void shouldAllowInternalWithNonInstitutionalEmail() {
        RegisterRequest request = baseRequestBuilder()
                .email("player@gmail.com")
                .userType(UserType.INTERNAL)
                .role(Role.PLAYER)
                .build();

        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    @DisplayName("Should reject CAPTAIN when email is not an institutional domain")
    void shouldRejectCaptainWithoutEscuelaingDomain() {
        RegisterRequest request = baseRequestBuilder()
                .email("captain@gmail.com")
                .role(Role.CAPTAIN)
                .build();

        assertThrows(ValidationException.class, () -> validator.validate(request));
    }

    @Test
    @DisplayName("Should allow CAPTAIN when email is @escuelaing.edu.co")
    void shouldAllowCaptainWithEscuelaingDomain() {
        RegisterRequest request = baseRequestBuilder()
                .email("capitan@escuelaing.edu.co")
                .role(Role.CAPTAIN)
                .build();

        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    @DisplayName("Should require only relationship description for EXTERNAL")
    void shouldAllowExternalWithoutRelationshipTypeButWithDescription() {
        RegisterRequest request = baseRequestBuilder()
                .userType(UserType.EXTERNAL)
                .relationshipType(null)
                .relationshipDescription("Familiar de estudiante")
                .build();

        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    @DisplayName("Should reject EXTERNAL without relationship description")
    void shouldRejectExternalWithoutRelationshipDescription() {
        RegisterRequest request = baseRequestBuilder()
                .userType(UserType.EXTERNAL)
                .relationshipDescription("   ")
                .build();

        assertThrows(ValidationException.class, () -> validator.validate(request));
    }

    @Test
    @DisplayName("Should reject weak password")
    void shouldRejectWeakPassword() {
        RegisterRequest request = baseRequestBuilder()
                .password("weakpass")
                .build();

        assertThrows(ValidationException.class, () -> validator.validate(request));
    }

    private RegisterRequest.RegisterRequestBuilder baseRequestBuilder() {
        return RegisterRequest.builder()
                .firstName("Test")
                .lastName("User")
                .username("testuser")
                .email("test@escuelaing.edu.co")
                .password("StrongPass1")
                .confirmPassword("StrongPass1")
                .userType(UserType.INTERNAL)
                .relationshipDescription("N/A")
                .role(Role.PLAYER);
    }
}
