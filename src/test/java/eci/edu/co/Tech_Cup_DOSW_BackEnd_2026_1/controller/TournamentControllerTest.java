package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.CourtRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentDateRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentSetupRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.CourtResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.ActiveTournamentInfoResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentBracketResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentCardEventResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentRulesConfirmationResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentStandingRowResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentTopScorerResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentMatchHistoryResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentMonthlyPerformanceResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentDateResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentSetupResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.TournamentStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.security.JwtService;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.interface_.TournamentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TournamentController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("TournamentController Tests")
class TournamentControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private TournamentService tournamentService;

        @MockBean
        private JwtService jwtService;

        private TournamentRequest tournamentRequest;
        private TournamentConfigRequest configRequest;
        private ChangeStatusRequest changeStatusRequest;
        private TournamentResponse tournamentResponse;

        @BeforeEach
        void setUp() {
                tournamentRequest = TournamentRequest.builder()
                                .name("Football Tournament 2026-1")
                                .startDate(LocalDate.of(2026, 3, 1))
                                .endDate(LocalDate.of(2026, 5, 31))
                                .teamCount(16)
                                .costPerTeam(500000.0)
                                .build();

                configRequest = TournamentConfigRequest.builder()
                                .startDate(LocalDate.of(2026, 3, 1))
                                .endDate(LocalDate.of(2026, 5, 31))
                                .teamCount(16)
                                .costPerTeam(500000.0)
                                .build();

                changeStatusRequest = ChangeStatusRequest.builder()
                                .status(TournamentStatus.ACTIVE)
                                .build();

                tournamentResponse = TournamentResponse.builder()
                                .id(1L)
                                .name("Football Tournament 2026-1")
                                .startDate(LocalDate.of(2026, 3, 1))
                                .endDate(LocalDate.of(2026, 5, 31))
                                .teamCount(16)
                                .costPerTeam(500000.0)
                                .status(TournamentStatus.DRAFT)
                                .build();
        }

        @Test
        @DisplayName("Should create tournament successfully")
        void testCreateTournamentSuccess() throws Exception {
                // Arrange
                when(tournamentService.create(any(TournamentRequest.class))).thenReturn(tournamentResponse);

                // Act & Assert
                mockMvc.perform(post("/api/tournaments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(tournamentRequest)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.name").value("Football Tournament 2026-1"));
        }

        @Test
        @DisplayName("Should get tournament by id successfully")
        void testGetTournamentByIdSuccess() throws Exception {
                // Arrange
                when(tournamentService.getById(anyLong())).thenReturn(tournamentResponse);

                // Act & Assert
                mockMvc.perform(get("/api/tournaments/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.teamCount").value(16));
        }

        @Test
        @DisplayName("Should get active tournament info successfully")
        void testGetActiveTournamentInfoSuccess() throws Exception {
                ActiveTournamentInfoResponse activeInfo = ActiveTournamentInfoResponse.builder()
                                .id(1L)
                                .name("Football Tournament 2026-1")
                                .startDate(LocalDate.of(2026, 3, 1))
                                .endDate(LocalDate.of(2026, 5, 31))
                                .status(TournamentStatus.IN_PROGRESS)
                                .registeredTeams(8)
                                .availableCourts(3)
                                .currentPhase("Semifinal")
                                .build();

                when(tournamentService.getActiveTournamentInfo()).thenReturn(activeInfo);

                mockMvc.perform(get("/api/tournaments/active")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.registeredTeams").value(8))
                                .andExpect(jsonPath("$.currentPhase").value("Semifinal"));
        }

        @Test
        @DisplayName("Should get tournament standings successfully")
        void testGetTournamentStandingsSuccess() throws Exception {
                List<TournamentStandingRowResponse> standings = List.of(
                                TournamentStandingRowResponse.builder()
                                                .pos(1)
                                                .teamId(10L)
                                                .team("Lions")
                                                .pj(5)
                                                .pg(4)
                                                .pe(1)
                                                .pp(0)
                                                .gf(12)
                                                .gc(4)
                                                .dc(8)
                                                .pts(13)
                                                .build());

                when(tournamentService.getTournamentStandings(1L)).thenReturn(standings);

                mockMvc.perform(get("/api/tournaments/1/standings")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].pos").value(1))
                                .andExpect(jsonPath("$[0].team").value("Lions"))
                                .andExpect(jsonPath("$[0].pts").value(13));
        }

        @Test
        @DisplayName("Should get tournament bracket successfully")
        void testGetTournamentBracketSuccess() throws Exception {
                TournamentBracketResponse bracket = TournamentBracketResponse.builder()
                                .tournamentId(1L)
                                .phases(List.of(
                                                TournamentBracketResponse.BracketPhaseResponse.builder()
                                                                .phase("Semifinal")
                                                                .classifiedTeams(List.of("Lions", "Tigers"))
                                                                .matches(List.of(
                                                                                TournamentBracketResponse.BracketMatchResponse
                                                                                                .builder()
                                                                                                .slot(1)
                                                                                                .matchId(101L)
                                                                                                .matchDate(LocalDate.of(
                                                                                                                2026, 5,
                                                                                                                20))
                                                                                                .matchTime(LocalTime.of(
                                                                                                                18, 0))
                                                                                                .homeTeamId(10L)
                                                                                                .homeTeam("Lions")
                                                                                                .awayTeamId(11L)
                                                                                                .awayTeam("Tigers")
                                                                                                .homeScore(2)
                                                                                                .awayScore(1)
                                                                                                .played(true)
                                                                                                .build()))
                                                                .build()))
                                .build();

                when(tournamentService.getTournamentBracket(1L)).thenReturn(bracket);

                mockMvc.perform(get("/api/tournaments/1/bracket")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.tournamentId").value(1))
                                .andExpect(jsonPath("$.phases[0].phase").value("Semifinal"))
                                .andExpect(jsonPath("$.phases[0].matches[0].homeTeam").value("Lions"));
        }

        @Test
        @DisplayName("Should get tournament cards history successfully")
        void testGetTournamentCardsSuccess() throws Exception {
                List<TournamentCardEventResponse> cards = List.of(
                                TournamentCardEventResponse.builder()
                                                .type("AMARILLA")
                                                .matchDate(LocalDate.of(2026, 5, 20))
                                                .matchTime(LocalTime.of(18, 0))
                                                .matchId(101L)
                                                .playerId(50L)
                                                .player("Juan Perez")
                                                .minute(35)
                                                .build());

                when(tournamentService.getTournamentCards(1L)).thenReturn(cards);

                mockMvc.perform(get("/api/tournaments/1/cards")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].type").value("AMARILLA"))
                                .andExpect(jsonPath("$[0].player").value("Juan Perez"))
                                .andExpect(jsonPath("$[0].minute").value(35));
        }

        @Test
        @DisplayName("Should get tournament top scorers successfully")
        void testGetTournamentTopScorersSuccess() throws Exception {
                List<TournamentTopScorerResponse> scorers = List.of(
                                TournamentTopScorerResponse.builder()
                                                .playerId(10L)
                                                .player("Juan Perez")
                                                .teamId(2L)
                                                .team("Lions")
                                                .playerPhotoUrl("https://cdn.example.com/players/10.jpg")
                                                .goals(8)
                                                .build());

                when(tournamentService.getTournamentTopScorers(1L)).thenReturn(scorers);

                mockMvc.perform(get("/api/tournaments/1/top-scorers")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].player").value("Juan Perez"))
                                .andExpect(jsonPath("$[0].team").value("Lions"))
                                .andExpect(jsonPath("$[0].goals").value(8));
        }

        @Test
        @DisplayName("Should get tournament match history successfully")
        void testGetTournamentMatchHistorySuccess() throws Exception {
                List<TournamentMatchHistoryResponse> history = List.of(
                                TournamentMatchHistoryResponse.builder()
                                                .matchId(101L)
                                                .matchDate(LocalDate.of(2026, 3, 10))
                                                .matchTime(LocalTime.of(18, 30))
                                                .phase("GROUP")
                                                .homeTeamId(2L)
                                                .homeTeam("Lions")
                                                .homeScore(2)
                                                .awayTeamId(3L)
                                                .awayTeam("Tigers")
                                                .awayScore(1)
                                                .build());

                when(tournamentService.getTournamentMatchHistory(1L)).thenReturn(history);

                mockMvc.perform(get("/api/tournaments/1/matches")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].matchId").value(101))
                                .andExpect(jsonPath("$[0].homeTeam").value("Lions"))
                                .andExpect(jsonPath("$[0].awayScore").value(1));
        }

        @Test
        @DisplayName("Should get tournament monthly performance successfully")
        void testGetTournamentMonthlyPerformanceSuccess() throws Exception {
                List<TournamentMonthlyPerformanceResponse> performance = List.of(
                                TournamentMonthlyPerformanceResponse.builder()
                                                .teamId(2L)
                                                .team("Lions")
                                                .monthly(List.of(
                                                                TournamentMonthlyPerformanceResponse.MonthlyPerformancePoint
                                                                                .builder()
                                                                                .month(1)
                                                                                .monthLabel("Enero")
                                                                                .points(6)
                                                                                .wins(2)
                                                                                .build(),
                                                                TournamentMonthlyPerformanceResponse.MonthlyPerformancePoint
                                                                                .builder()
                                                                                .month(2)
                                                                                .monthLabel("Febrero")
                                                                                .points(4)
                                                                                .wins(1)
                                                                                .build()))
                                                .build());

                when(tournamentService.getTournamentMonthlyPerformance(1L)).thenReturn(performance);

                mockMvc.perform(get("/api/tournaments/1/performance")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].team").value("Lions"))
                                .andExpect(jsonPath("$[0].monthly[0].month").value(1))
                                .andExpect(jsonPath("$[0].monthly[0].points").value(6));
        }

        @Test
        @DisplayName("Should confirm tournament rules successfully")
        void testConfirmTournamentRulesSuccess() throws Exception {
                TournamentRulesConfirmationResponse confirmation = TournamentRulesConfirmationResponse.builder()
                                .tournamentId(1L)
                                .userId(5L)
                                .confirmedAt(LocalDateTime.of(2026, 4, 13, 19, 30))
                                .rulesConfirmed(true)
                                .build();

                when(tournamentService.confirmTournamentRules(anyLong(), any())).thenReturn(confirmation);

                mockMvc.perform(post("/api/tournaments/1/confirm-rules")
                                .principal(new UsernamePasswordAuthenticationToken("admin@escuelaing.edu.co", "N/A"))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.tournamentId").value(1))
                                .andExpect(jsonPath("$.userId").value(5))
                                .andExpect(jsonPath("$.rulesConfirmed").value(true));
        }

        @Test
        @DisplayName("Should return 404 when tournament not found")
        void testGetTournamentByIdNotFound() throws Exception {
                // Arrange
                when(tournamentService.getById(anyLong()))
                                .thenThrow(new ResourceNotFoundException("Tournament not found"));

                // Act & Assert
                mockMvc.perform(get("/api/tournaments/999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should change tournament status successfully")
        void testChangeStatusSuccess() throws Exception {
                // Arrange
                tournamentResponse.setStatus(TournamentStatus.ACTIVE);
                when(tournamentService.changeStatus(anyLong(), any(ChangeStatusRequest.class)))
                                .thenReturn(tournamentResponse);

                // Act & Assert
                mockMvc.perform(patch("/api/tournaments/1/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(changeStatusRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.status").value("ACTIVE"));
        }

        @Test
        @DisplayName("Should return 404 when changing status of non-existent tournament")
        void testChangeStatusNotFound() throws Exception {
                // Arrange
                when(tournamentService.changeStatus(anyLong(), any(ChangeStatusRequest.class)))
                                .thenThrow(new ResourceNotFoundException("Tournament not found"));

                // Act & Assert
                mockMvc.perform(patch("/api/tournaments/999/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(changeStatusRequest)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should configure tournament successfully")
        void testConfigureTournamentSuccess() throws Exception {
                // Arrange
                when(tournamentService.configure(anyLong(), any(TournamentConfigRequest.class)))
                                .thenReturn(tournamentResponse);

                // Act & Assert
                mockMvc.perform(put("/api/tournaments/1/config")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(configRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        @DisplayName("Should return 404 when configuring non-existent tournament")
        void testConfigureTournamentNotFound() throws Exception {
                // Arrange
                when(tournamentService.configure(anyLong(), any(TournamentConfigRequest.class)))
                                .thenThrow(new ResourceNotFoundException("Tournament not found"));

                // Act & Assert
                mockMvc.perform(put("/api/tournaments/999/config")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(configRequest)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should setup tournament successfully")
        void testSetupTournamentSuccess() throws Exception {
                TournamentSetupRequest setupRequest = TournamentSetupRequest.builder()
                                .rules("Partidos de 2 tiempos de 25 minutos.")
                                .sanctionRules("Tarjeta roja = 2 partidos")
                                .inscriptionCloseDate(LocalDate.of(2026, 3, 20))
                                .courts(List.of(
                                                CourtRequest.builder().name("Cancha Principal").location("Bloque B")
                                                                .build()))
                                .schedule(List.of(
                                                TournamentDateRequest.builder()
                                                                .description("Jornada 1")
                                                                .eventDate(LocalDate.of(2026, 4, 15)).build()))
                                .build();

                TournamentSetupResponse setupResponse = TournamentSetupResponse.builder()
                                .tournamentId(1L)
                                .tournamentName("Football Tournament 2026-1")
                                .rules("Partidos de 2 tiempos de 25 minutos.")
                                .sanctionRules("Tarjeta roja = 2 partidos")
                                .courts(List.of(CourtResponse.builder()
                                                .id(1L).name("Cancha Principal").location("Bloque B").build()))
                                .schedule(List.of(TournamentDateResponse.builder()
                                                .id(1L).description("Jornada 1")
                                                .eventDate(LocalDate.of(2026, 4, 15)).build()))
                                .build();

                when(tournamentService.setup(anyLong(), any(TournamentSetupRequest.class)))
                                .thenReturn(setupResponse);

                mockMvc.perform(put("/api/tournaments/1/setup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(setupRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.tournamentId").value(1))
                                .andExpect(jsonPath("$.rules").value("Partidos de 2 tiempos de 25 minutos."))
                                .andExpect(jsonPath("$.courts[0].name").value("Cancha Principal"))
                                .andExpect(jsonPath("$.schedule[0].description").value("Jornada 1"));
        }

        @Test
        @DisplayName("Should return 404 when setting up non-existent tournament")
        void testSetupTournamentNotFound() throws Exception {
                TournamentSetupRequest setupRequest = TournamentSetupRequest.builder()
                                .rules("Reglas")
                                .courts(List.of(CourtRequest.builder().name("C1").location("L1").build()))
                                .schedule(List.of(TournamentDateRequest.builder()
                                                .description("J1").eventDate(LocalDate.of(2026, 4, 15)).build()))
                                .build();

                when(tournamentService.setup(anyLong(), any(TournamentSetupRequest.class)))
                                .thenThrow(new ResourceNotFoundException("Tournament not found"));

                mockMvc.perform(put("/api/tournaments/999/setup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(setupRequest)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 400 when setup request has validation errors")
        void testSetupTournamentValidationError() throws Exception {
                TournamentSetupRequest setupRequest = TournamentSetupRequest.builder()
                                .rules("Reglas")
                                .courts(List.of(CourtRequest.builder().name("C1").location("L1").build()))
                                .schedule(List.of(TournamentDateRequest.builder()
                                                .description("J1").eventDate(LocalDate.of(2026, 4, 15)).build()))
                                .build();

                when(tournamentService.setup(anyLong(), any(TournamentSetupRequest.class)))
                                .thenThrow(new ValidationException("Errores de validacion",
                                                Map.of("rules", "El reglamento es requerido")));

                mockMvc.perform(put("/api/tournaments/1/setup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(setupRequest)))
                                .andExpect(status().isBadRequest());
        }
}
