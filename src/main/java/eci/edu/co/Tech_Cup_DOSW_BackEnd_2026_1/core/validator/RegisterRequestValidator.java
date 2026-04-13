package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.RegisterRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.Role;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.UserType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.util.InstitutionEmailUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RegisterRequestValidator {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final String EMAIL_REGEX = "^[\\w.+-]+@[\\w-]+\\.[\\w.]+$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9._-]{3,30}$";

    public void validate(RegisterRequest request) {
        Map<String, String> errors = new HashMap<>();

        // Validar nombre
        if (request.getFirstName() == null || request.getFirstName().isBlank()) {
            errors.put("firstName", "El nombre es requerido");
        }

        // Validar apellido
        if (request.getLastName() == null || request.getLastName().isBlank()) {
            errors.put("lastName", "El apellido es requerido");
        }

        // Validar username
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            errors.put("username", "El nombre de usuario es requerido");
        } else if (!request.getUsername().matches(USERNAME_REGEX)) {
            errors.put("username", "El usuario debe tener entre 3-30 caracteres (letras, números, . _ -)");
        }

        // Validar email
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            errors.put("email", "El email es requerido");
        } else if (!request.getEmail().matches(EMAIL_REGEX)) {
            errors.put("email", "El formato del email es inválido");
        } else {
            // Validar tipo de usuario según dominio
            validateUserTypeByEmail(request, errors);
        }

        // Validar contraseña
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            errors.put("password", "La contraseña es requerida");
        } else if (request.getPassword().length() < MIN_PASSWORD_LENGTH) {
            errors.put("password", "La contraseña debe tener al menos " + MIN_PASSWORD_LENGTH + " caracteres");
        }

        // Validar confirmación de contraseña
        if (request.getConfirmPassword() == null || request.getConfirmPassword().isBlank()) {
            errors.put("confirmPassword", "Debe confirmar la contraseña");
        } else if (!request.getPassword().equals(request.getConfirmPassword())) {
            errors.put("confirmPassword", "Las contraseñas no coinciden");
        }

        // Validar tipo de usuario
        if (request.getUserType() == null) {
            errors.put("userType", "El tipo de usuario es requerido");
        } else if (request.getUserType() == UserType.EXTERNAL) {
            // Si es externo, validar relación
            if (request.getRelationshipType() == null || request.getRelationshipType().isBlank()) {
                errors.put("relationshipType", "Debe especificar la relación con la escuela (Familiar/Invitado)");
            }
            if (request.getRelationshipDescription() == null || request.getRelationshipDescription().isBlank()) {
                errors.put("relationshipDescription", "Debe describir su relación con la escuela");
            } else if (request.getRelationshipDescription().length() < 5) {
                errors.put("relationshipDescription", "La descripción debe tener al menos 5 caracteres");
            }
        }

        // Validar rol
        if (request.getRole() == null) {
            errors.put("role", "El rol es requerido");
        } else if (request.getRole() == Role.CAPTAIN
                && !InstitutionEmailUtils.isValidCaptainInstitutionalEmailFormat(request.getEmail())) {
            errors.put("email",
                    "Para CAPTAIN el correo debe ser institucional con formato valido, por ejemplo: "
                            + "XXX.XXX-X@escuelaing.edu.co, XXX.XXX-X@mail.escuelaing.edu.co o XXX.XXX@escuelaing.edu.co");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Errores de validación en el registro", errors);
        }
    }

    private void validateUserTypeByEmail(RegisterRequest request, Map<String, String> errors) {
        boolean isInternalEmail = InstitutionEmailUtils.isEscuelaingEmail(request.getEmail());

        // Si el email es de la escuela pero dice que es EXTERNAL, advertir
        if (isInternalEmail && request.getUserType() == UserType.EXTERNAL) {
            errors.put("userType",
                    "Los correos institucionales @escuelaing.edu.co o @mail.escuelaing.edu.co deben registrarse como INTERNO");
        }

        // Si el email NO es de la escuela pero dice que es INTERNAL sin tener email
        // @escuelaing
        if (!isInternalEmail && request.getUserType() == UserType.INTERNAL) {
            errors.put("userType",
                    "Los usuarios INTERNOS deben usar correo institucional @escuelaing.edu.co o @mail.escuelaing.edu.co");
        }
    }
}
