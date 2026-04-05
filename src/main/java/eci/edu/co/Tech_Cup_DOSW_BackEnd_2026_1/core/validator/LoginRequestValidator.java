package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.LoginRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LoginRequestValidator {

    public void validate(LoginRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            errors.put("email", "El email es requerido");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            errors.put("password", "La contrasena es requerida");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Errores de validacion en el login", errors);
        }
    }
}
