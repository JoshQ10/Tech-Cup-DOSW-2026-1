package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class RegisterRequestValidator {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final String PASSWORD_UPPERCASE_REGEX = ".*[A-Z].*";
    private static final String PASSWORD_LOWERCASE_REGEX = ".*[a-z].*";
    private static final String PASSWORD_DIGIT_REGEX = ".*[0-9].*";
    private static final String CAPTAIN_INSTITUTIONAL_DOMAIN = "@escuelaing.edu.co";

    public void validate(RegisterRequest request) {
        Map<String, String> errors = new HashMap<>();

        // Validar username (unicidad se valida en AuthService)
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            errors.put("username", "El nombre de usuario es requerido");
        }

        // Validar email (unicidad se valida en AuthService)
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            errors.put("email", "El email es requerido");
        }

        // Validar contraseña
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            errors.put("password", "La contraseña es requerida");
        } else if (request.getPassword().length() < MIN_PASSWORD_LENGTH) {
            errors.put("password", "La contraseña debe tener al menos " + MIN_PASSWORD_LENGTH + " caracteres");
        } else if (!request.getPassword().matches(PASSWORD_UPPERCASE_REGEX)) {
            errors.put("password", "La contraseña debe contener al menos una letra mayúscula");
        } else if (!request.getPassword().matches(PASSWORD_LOWERCASE_REGEX)) {
            errors.put("password", "La contraseña debe contener al menos una letra minúscula");
        } else if (!request.getPassword().matches(PASSWORD_DIGIT_REGEX)) {
            errors.put("password", "La contraseña debe contener al menos un número");
        }

        // Si es EXTERNAL, validar que describa su relación con la escuela
        if (request.getUserType() == UserType.EXTERNAL) {
            if (request.getRelationshipDescription() == null || request.getRelationshipDescription().isBlank()) {
                errors.put("relationshipDescription", "Debe describir su relación con la escuela");
            }
        }

        // Validar rol
        if (request.getRole() == null) {
            errors.put("role", "El rol es requerido");
        } else if (request.getRole() == Role.CAPTAIN && !isCaptainInstitutionalEmail(request.getEmail())) {
            errors.put("email",
                    "Para CAPTAIN el correo debe ser institucional y terminar en @escuelaing.edu.co");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Errores de validación en el registro", errors);
        }
    }

    private boolean isCaptainInstitutionalEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return email.toLowerCase(Locale.ROOT).endsWith(CAPTAIN_INSTITUTIONAL_DOMAIN);
    }
}
