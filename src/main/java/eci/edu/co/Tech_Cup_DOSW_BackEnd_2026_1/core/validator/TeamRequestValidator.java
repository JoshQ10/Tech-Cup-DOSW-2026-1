package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TeamRequestValidator {

    private final UserRepository userRepository;

    public void validate(TeamRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (request.getName() == null || request.getName().isBlank()) {
            errors.put("name", "El nombre del equipo es requerido");
        }

        if (request.getTournamentId() == null) {
            errors.put("tournamentId", "El ID del torneo es requerido");
        }

        if (request.getCaptainId() == null) {
            errors.put("captainId", "El ID del capitan es requerido");
        } else {
            validateCaptainIsInternal(request.getCaptainId(), errors);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Errores de validacion en la creacion del equipo", errors);
        }
    }

    private void validateCaptainIsInternal(Long captainId, Map<String, String> errors) {
        User captain = userRepository.findById(captainId)
                .orElse(null);

        if (captain == null) {
            errors.put("captainId", "El capitán especificado no existe");
            return;
        }

        String email = captain.getEmail();
        if (!isInternalEmail(email)) {
            errors.put("captainId", "El capitán debe ser un usuario interno de la escuela. " +
                    "Su email debe terminar con @mail.escuelaing.edu.co o @escuelaing.edu.co");
        }
    }

    private boolean isInternalEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return email.endsWith("@mail.escuelaing.edu.co") || email.endsWith("@escuelaing.edu.co");
    }
}
