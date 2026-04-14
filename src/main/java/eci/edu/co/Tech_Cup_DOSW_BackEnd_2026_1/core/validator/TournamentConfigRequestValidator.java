package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TournamentConfigRequestValidator {

    private static final int MIN_TEAMS = 2;

    public void validate(TournamentConfigRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (request.getStartDate() == null) {
            errors.put("startDate", "La fecha de inicio es requerida");
        }

        if (request.getEndDate() == null) {
            errors.put("endDate", "La fecha de fin es requerida");
        }

        if (request.getStartDate() != null && request.getEndDate() != null
                && request.getEndDate().isBefore(request.getStartDate())) {
            errors.put("endDate", "La fecha de fin debe ser posterior a la fecha de inicio");
        }

        if (request.getTeamCount() < MIN_TEAMS) {
            errors.put("teamCount", "El torneo debe tener al menos " + MIN_TEAMS + " equipos");
        }

        if (request.getCostPerTeam() < 0) {
            errors.put("costPerTeam", "El costo por equipo no puede ser negativo");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Errores de validacion en la configuracion del torneo", errors);
        }
    }
}
