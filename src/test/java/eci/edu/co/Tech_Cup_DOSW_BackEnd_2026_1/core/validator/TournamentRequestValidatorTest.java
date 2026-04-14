package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TournamentRequestValidator Tests")
class TournamentRequestValidatorTest {

    private TournamentRequestValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TournamentRequestValidator();
    }

    private TournamentRequest validRequest() {
        return TournamentRequest.builder()
                .name("Copa DOSW 2026")
                .startDate(LocalDate.of(2026, 4, 1))
                .endDate(LocalDate.of(2026, 6, 30))
                .teamCount(8)
                .costPerTeam(300000.0)
                .build();
    }

    @Test
    @DisplayName("Debe pasar validacion con datos validos")
    void testValidRequestPasses() {
        assertDoesNotThrow(() -> validator.validate(validRequest()));
    }

    @Nested
    @DisplayName("Validacion de nombre")
    class NameValidation {

        @Test
        @DisplayName("Debe fallar con nombre nulo")
        void testNullName() {
            TournamentRequest request = validRequest();
            request.setName(null);

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("name"));
        }

        @Test
        @DisplayName("Debe fallar con nombre vacio")
        void testBlankName() {
            TournamentRequest request = validRequest();
            request.setName("   ");

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("name"));
        }
    }

    @Nested
    @DisplayName("Validacion de fechas")
    class DateValidation {

        @Test
        @DisplayName("Debe fallar con fecha de inicio nula")
        void testNullStartDate() {
            TournamentRequest request = validRequest();
            request.setStartDate(null);

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("startDate"));
        }

        @Test
        @DisplayName("Debe fallar con fecha de fin nula")
        void testNullEndDate() {
            TournamentRequest request = validRequest();
            request.setEndDate(null);

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("endDate"));
        }

        @Test
        @DisplayName("Debe fallar cuando fecha de fin es anterior a fecha de inicio")
        void testEndDateBeforeStartDate() {
            TournamentRequest request = validRequest();
            request.setStartDate(LocalDate.of(2026, 6, 1));
            request.setEndDate(LocalDate.of(2026, 3, 1));

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("endDate"));
        }

        @Test
        @DisplayName("Debe pasar cuando fecha de inicio y fin son iguales")
        void testSameStartAndEndDate() {
            TournamentRequest request = validRequest();
            LocalDate sameDate = LocalDate.of(2026, 5, 1);
            request.setStartDate(sameDate);
            request.setEndDate(sameDate);

            assertDoesNotThrow(() -> validator.validate(request));
        }
    }

    @Nested
    @DisplayName("Validacion de cantidad de equipos")
    class TeamCountValidation {

        @Test
        @DisplayName("Debe fallar con menos de 2 equipos")
        void testTeamCountLessThanMinimum() {
            TournamentRequest request = validRequest();
            request.setTeamCount(1);

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("teamCount"));
        }

        @Test
        @DisplayName("Debe fallar con 0 equipos")
        void testTeamCountZero() {
            TournamentRequest request = validRequest();
            request.setTeamCount(0);

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("teamCount"));
        }

        @Test
        @DisplayName("Debe pasar con exactamente 2 equipos")
        void testTeamCountMinimum() {
            TournamentRequest request = validRequest();
            request.setTeamCount(2);

            assertDoesNotThrow(() -> validator.validate(request));
        }
    }

    @Nested
    @DisplayName("Validacion de costo por equipo")
    class CostValidation {

        @Test
        @DisplayName("Debe fallar con costo negativo")
        void testNegativeCost() {
            TournamentRequest request = validRequest();
            request.setCostPerTeam(-100.0);

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("costPerTeam"));
        }

        @Test
        @DisplayName("Debe pasar con costo cero (torneo gratuito)")
        void testZeroCost() {
            TournamentRequest request = validRequest();
            request.setCostPerTeam(0.0);

            assertDoesNotThrow(() -> validator.validate(request));
        }
    }

    @Test
    @DisplayName("Debe reportar multiples errores a la vez")
    void testMultipleErrors() {
        TournamentRequest request = TournamentRequest.builder()
                .name(null)
                .startDate(null)
                .endDate(null)
                .teamCount(0)
                .costPerTeam(-1)
                .build();

        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(request));
        assertTrue(ex.getFieldErrors().size() >= 4);
    }
}
