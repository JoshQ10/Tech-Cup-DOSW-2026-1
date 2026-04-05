package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.CourtRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentDateRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentSetupRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.CourtResponse;
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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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
                                                CourtRequest.builder().name("Cancha Principal").location("Bloque B").build()))
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
