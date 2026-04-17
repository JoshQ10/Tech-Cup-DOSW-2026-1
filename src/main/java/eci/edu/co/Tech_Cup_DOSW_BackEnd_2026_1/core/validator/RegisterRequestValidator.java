package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.InstitutionEmailUtils;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class RegisterRequestValidator {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final String PASSWORD_UPPERCASE_REGEX = ".*[A-Z].*";
    private static final String PASSWORD_LOWERCASE_REGEX = ".*[a-z].*";
    private static final String PASSWORD_DIGIT_REGEX = ".*[0-9].*";
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$");
    private static final Pattern IDENTIFICATION_NUMERIC_PATTERN = Pattern.compile("^\\d+$");
    private static final Set<Role> PRIVILEGED_ROLES = EnumSet.of(Role.ADMINISTRATOR, Role.REFEREE, Role.ORGANIZER);

    public void validate(RegisterRequest request) {
        Map<String, String> errors = new HashMap<>();

        // Validar username (unicidad se valida en AuthService)
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            errors.put("username", "El nombre de usuario es requerido");
        }

        // Validar email (unicidad se valida en AuthService)
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            errors.put("email", "El email es requerido");
        } else if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            errors.put("email", "El formato del email no es válido");
        }

        // Validar contraseña y confirmación
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
        } else if (!request.getPassword().equals(request.getConfirmPassword())) {
            errors.put("confirmPassword", "La confirmación de contraseña no coincide");
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
        } else if (request.getRole() == Role.CAPTAIN
                && !InstitutionEmailUtils.isEscuelaingEmail(request.getEmail())) {
            errors.put("email",
                    "Para CAPTAIN el correo debe ser institucional (@escuelaing.edu.co o @mail.escuelaing.edu.co)");
        } else if (PRIVILEGED_ROLES.contains(request.getRole())) {
            // Para roles privilegiados se requiere cédula válida
            String identification = request.getIdentification();
            if (identification == null || identification.isBlank()) {
                errors.put("identification",
                        "La cédula es obligatoria para roles ADMINISTRATOR, REFEREE y ORGANIZER");
            } else if (!IDENTIFICATION_NUMERIC_PATTERN.matcher(identification.trim()).matches()) {
                errors.put("identification", "La cédula debe contener únicamente números");
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Errores de validación en el registro", errors);
        }
    }
}
