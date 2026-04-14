package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.TournamentStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ChangeStatusRequestValidator Tests")
class ChangeStatusRequestValidatorTest {

    private ChangeStatusRequestValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ChangeStatusRequestValidator();
    }

    @Nested
    @DisplayName("Transiciones validas")
    class ValidTransitions {

        @Test
        @DisplayName("DRAFT -> ACTIVE es valido")
        void testDraftToActive() {
            assertDoesNotThrow(() ->
                    validator.validate(TournamentStatus.DRAFT, TournamentStatus.ACTIVE));
        }

        @Test
        @DisplayName("ACTIVE -> IN_PROGRESS es valido")
        void testActiveToInProgress() {
            assertDoesNotThrow(() ->
                    validator.validate(TournamentStatus.ACTIVE, TournamentStatus.IN_PROGRESS));
        }

        @Test
        @DisplayName("IN_PROGRESS -> FINISHED es valido")
        void testInProgressToFinished() {
            assertDoesNotThrow(() ->
                    validator.validate(TournamentStatus.IN_PROGRESS, TournamentStatus.FINISHED));
        }
    }

    @Nested
    @DisplayName("Transiciones invalidas")
    class InvalidTransitions {

        @Test
        @DisplayName("DRAFT -> IN_PROGRESS no es valido (saltar paso)")
        void testDraftToInProgress() {
            BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                    () -> validator.validate(TournamentStatus.DRAFT, TournamentStatus.IN_PROGRESS));
            assertTrue(ex.getMessage().contains("Transicion de estado no permitida"));
        }

        @Test
        @DisplayName("DRAFT -> FINISHED no es valido (saltar pasos)")
        void testDraftToFinished() {
            assertThrows(BusinessRuleException.class,
                    () -> validator.validate(TournamentStatus.DRAFT, TournamentStatus.FINISHED));
        }

        @Test
        @DisplayName("ACTIVE -> DRAFT no es valido (retroceder)")
        void testActiveToDraft() {
            assertThrows(BusinessRuleException.class,
                    () -> validator.validate(TournamentStatus.ACTIVE, TournamentStatus.DRAFT));
        }

        @Test
        @DisplayName("ACTIVE -> FINISHED no es valido (saltar paso)")
        void testActiveToFinished() {
            assertThrows(BusinessRuleException.class,
                    () -> validator.validate(TournamentStatus.ACTIVE, TournamentStatus.FINISHED));
        }

        @Test
        @DisplayName("IN_PROGRESS -> DRAFT no es valido (retroceder)")
        void testInProgressToDraft() {
            assertThrows(BusinessRuleException.class,
                    () -> validator.validate(TournamentStatus.IN_PROGRESS, TournamentStatus.DRAFT));
        }

        @Test
        @DisplayName("IN_PROGRESS -> ACTIVE no es valido (retroceder)")
        void testInProgressToActive() {
            assertThrows(BusinessRuleException.class,
                    () -> validator.validate(TournamentStatus.IN_PROGRESS, TournamentStatus.ACTIVE));
        }

        @Test
        @DisplayName("FINISHED no permite ninguna transicion")
        void testFinishedToAny() {
            for (TournamentStatus target : TournamentStatus.values()) {
                if (target == TournamentStatus.FINISHED) continue;
                assertThrows(BusinessRuleException.class,
                        () -> validator.validate(TournamentStatus.FINISHED, target),
                        "FINISHED -> " + target + " deberia fallar");
            }
        }
    }

    @Nested
    @DisplayName("Casos especiales")
    class EdgeCases {

        @Test
        @DisplayName("Mismo estado lanza BusinessRuleException")
        void testSameStatus() {
            BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                    () -> validator.validate(TournamentStatus.ACTIVE, TournamentStatus.ACTIVE));
            assertTrue(ex.getMessage().contains("ya se encuentra en estado"));
        }

        @Test
        @DisplayName("Nuevo estado nulo lanza ValidationException")
        void testNullNewStatus() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(TournamentStatus.DRAFT, null));
            assertTrue(ex.getFieldErrors().containsKey("status"));
        }
    }
}
