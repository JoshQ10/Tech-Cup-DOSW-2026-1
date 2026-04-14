package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.TournamentStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class ChangeStatusRequestValidator {

    private static final Map<TournamentStatus, Set<TournamentStatus>> ALLOWED_TRANSITIONS = Map.of(
            TournamentStatus.DRAFT, Set.of(TournamentStatus.ACTIVE),
            TournamentStatus.ACTIVE, Set.of(TournamentStatus.IN_PROGRESS),
            TournamentStatus.IN_PROGRESS, Set.of(TournamentStatus.FINISHED),
            TournamentStatus.FINISHED, Set.of()
    );

    public void validate(TournamentStatus currentStatus, TournamentStatus newStatus) {
        if (newStatus == null) {
            throw new ValidationException("Errores de validacion en el cambio de estado",
                    Map.of("status", "El nuevo estado es requerido"));
        }

        if (currentStatus == newStatus) {
            throw new BusinessRuleException(
                    "El torneo ya se encuentra en estado " + currentStatus);
        }

        Set<TournamentStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(currentStatus, Set.of());
        if (!allowed.contains(newStatus)) {
            throw new BusinessRuleException(
                    "Transicion de estado no permitida: " + currentStatus + " -> " + newStatus
                    + ". Transiciones validas desde " + currentStatus + ": " + allowed);
        }
    }
}
