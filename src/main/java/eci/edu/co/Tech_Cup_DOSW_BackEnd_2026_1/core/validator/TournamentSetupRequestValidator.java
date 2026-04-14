package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.CourtRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentDateRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentSetupRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TournamentSetupRequestValidator {

    public void validate(TournamentSetupRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (request.getRules() == null || request.getRules().isBlank()) {
            errors.put("rules", "El reglamento es requerido");
        }

        validateCourts(request.getCourts(), errors);
        validateSchedule(request.getSchedule(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException("Errores de validacion en la configuracion del torneo", errors);
        }
    }

    private void validateCourts(List<CourtRequest> courts, Map<String, String> errors) {
        if (courts == null || courts.isEmpty()) {
            errors.put("courts", "Debe registrar al menos una cancha");
            return;
        }

        for (int i = 0; i < courts.size(); i++) {
            CourtRequest court = courts.get(i);
            if (court.getName() == null || court.getName().isBlank()) {
                errors.put("courts[" + i + "].name", "El nombre de la cancha es requerido");
            }
            if (court.getLocation() == null || court.getLocation().isBlank()) {
                errors.put("courts[" + i + "].location", "La ubicacion de la cancha es requerida");
            }
        }
    }

    private void validateSchedule(List<TournamentDateRequest> schedule, Map<String, String> errors) {
        if (schedule == null || schedule.isEmpty()) {
            errors.put("schedule", "Debe registrar al menos una fecha");
            return;
        }

        for (int i = 0; i < schedule.size(); i++) {
            TournamentDateRequest date = schedule.get(i);
            if (date.getDescription() == null || date.getDescription().isBlank()) {
                errors.put("schedule[" + i + "].description", "La descripcion de la fecha es requerida");
            }
            if (date.getEventDate() == null) {
                errors.put("schedule[" + i + "].eventDate", "La fecha del evento es requerida");
            }
        }
    }
}
