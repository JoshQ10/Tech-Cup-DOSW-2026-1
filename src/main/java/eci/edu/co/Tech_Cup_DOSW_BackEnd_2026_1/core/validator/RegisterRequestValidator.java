package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RegisterRequestValidator {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final String EMAIL_REGEX = "^[\\w.+-]+@[\\w-]+\\.[\\w.]+$";

    public void validate(RegisterRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (request.getName() == null || request.getName().isBlank()) {
            errors.put("name", "El nombre es requerido");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            errors.put("email", "El email es requerido");
        } else if (!request.getEmail().matches(EMAIL_REGEX)) {
            errors.put("email", "El formato del email es invalido");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            errors.put("password", "La contrasena es requerida");
        } else if (request.getPassword().length() < MIN_PASSWORD_LENGTH) {
            errors.put("password", "La contrasena debe tener al menos " + MIN_PASSWORD_LENGTH + " caracteres");
        }

        if (request.getRole() == null) {
            errors.put("role", "El rol es requerido");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Errores de validacion en el registro", errors);
        }
    }
}
