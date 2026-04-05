package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.CourtRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentDateRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentSetupRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TournamentSetupRequestValidator Tests")
class TournamentSetupRequestValidatorTest {

    private TournamentSetupRequestValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TournamentSetupRequestValidator();
    }

    private TournamentSetupRequest validRequest() {
        return TournamentSetupRequest.builder()
                .rules("Partidos de 2 tiempos de 25 minutos.")
                .sanctionRules("Tarjeta roja = 2 partidos de suspension")
                .inscriptionCloseDate(LocalDate.of(2026, 3, 20))
                .courts(List.of(
                        CourtRequest.builder().name("Cancha Principal").location("Bloque B").build()
                ))
                .schedule(List.of(
                        TournamentDateRequest.builder()
                                .description("Jornada 1")
                                .eventDate(LocalDate.of(2026, 4, 15))
                                .build()
                ))
                .build();
    }

    @Test
    @DisplayName("Debe pasar validacion con datos validos")
    void testValidRequestPasses() {
        assertDoesNotThrow(() -> validator.validate(validRequest()));
    }

    @Nested
    @DisplayName("Validacion de reglamento")
    class RulesValidation {

        @Test
        @DisplayName("Debe fallar con reglamento nulo")
        void testNullRules() {
            TournamentSetupRequest request = validRequest();
            request.setRules(null);

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("rules"));
        }

        @Test
        @DisplayName("Debe fallar con reglamento vacio")
        void testBlankRules() {
            TournamentSetupRequest request = validRequest();
            request.setRules("   ");

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("rules"));
        }
    }

    @Nested
    @DisplayName("Validacion de canchas")
    class CourtsValidation {

        @Test
        @DisplayName("Debe fallar con lista de canchas nula")
        void testNullCourts() {
            TournamentSetupRequest request = validRequest();
            request.setCourts(null);

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("courts"));
        }

        @Test
        @DisplayName("Debe fallar con lista de canchas vacia")
        void testEmptyCourts() {
            TournamentSetupRequest request = validRequest();
            request.setCourts(Collections.emptyList());

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("courts"));
        }

        @Test
        @DisplayName("Debe fallar con nombre de cancha vacio")
        void testBlankCourtName() {
            TournamentSetupRequest request = validRequest();
            request.setCourts(List.of(
                    CourtRequest.builder().name("").location("Bloque B").build()
            ));

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("courts[0].name"));
        }

        @Test
        @DisplayName("Debe fallar con ubicacion de cancha vacia")
        void testBlankCourtLocation() {
            TournamentSetupRequest request = validRequest();
            request.setCourts(List.of(
                    CourtRequest.builder().name("Cancha 1").location(null).build()
            ));

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("courts[0].location"));
        }
    }

    @Nested
    @DisplayName("Validacion de horarios")
    class ScheduleValidation {

        @Test
        @DisplayName("Debe fallar con lista de fechas nula")
        void testNullSchedule() {
            TournamentSetupRequest request = validRequest();
            request.setSchedule(null);

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("schedule"));
        }

        @Test
        @DisplayName("Debe fallar con lista de fechas vacia")
        void testEmptySchedule() {
            TournamentSetupRequest request = validRequest();
            request.setSchedule(Collections.emptyList());

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("schedule"));
        }

        @Test
        @DisplayName("Debe fallar con descripcion de fecha vacia")
        void testBlankDateDescription() {
            TournamentSetupRequest request = validRequest();
            request.setSchedule(List.of(
                    TournamentDateRequest.builder()
                            .description("")
                            .eventDate(LocalDate.of(2026, 4, 15))
                            .build()
            ));

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("schedule[0].description"));
        }

        @Test
        @DisplayName("Debe fallar con fecha de evento nula")
        void testNullEventDate() {
            TournamentSetupRequest request = validRequest();
            request.setSchedule(List.of(
                    TournamentDateRequest.builder()
                            .description("Jornada 1")
                            .eventDate(null)
                            .build()
            ));

            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validate(request));
            assertTrue(ex.getFieldErrors().containsKey("schedule[0].eventDate"));
        }
    }

    @Test
    @DisplayName("Debe reportar multiples errores a la vez")
    void testMultipleErrors() {
        TournamentSetupRequest request = TournamentSetupRequest.builder()
                .rules(null)
                .courts(null)
                .schedule(null)
                .build();

        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(request));
        assertTrue(ex.getFieldErrors().size() >= 3);
    }

    @Test
    @DisplayName("Debe pasar sin sanctionRules ni inscriptionCloseDate (opcionales)")
    void testOptionalFieldsCanBeNull() {
        TournamentSetupRequest request = validRequest();
        request.setSanctionRules(null);
        request.setInscriptionCloseDate(null);

        assertDoesNotThrow(() -> validator.validate(request));
    }
}
