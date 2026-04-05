package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TeamRequestValidator {

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
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Errores de validacion en la creacion del equipo", errors);
        }
    }
}
