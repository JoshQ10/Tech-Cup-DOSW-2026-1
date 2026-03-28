package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.TournamentStatus;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

                changeStatusRequest = ChangeStatusRequest.builder()
                                .status(TournamentStatus.ACTIVE)
                                .build();

                tournamentResponse = TournamentResponse.builder()
                                .id("tournament123")
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
                                .andExpect(jsonPath("$.id").value("tournament123"))
                                .andExpect(jsonPath("$.name").value("Football Tournament 2026-1"));
        }

        @Test
        @DisplayName("Should get tournament by id successfully")
        void testGetTournamentByIdSuccess() throws Exception {
                // Arrange
                when(tournamentService.getById(anyString())).thenReturn(tournamentResponse);

                // Act & Assert
                mockMvc.perform(get("/api/tournaments/tournament123")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value("tournament123"))
                                .andExpect(jsonPath("$.teamCount").value(16));
        }

        @Test
        @DisplayName("Should return 404 when tournament not found")
        void testGetTournamentByIdNotFound() throws Exception {
                // Arrange
                when(tournamentService.getById(anyString()))
                                .thenThrow(new ResourceNotFoundException("Tournament not found"));

                // Act & Assert
                mockMvc.perform(get("/api/tournaments/tournament999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should change tournament status successfully")
        void testChangeStatusSuccess() throws Exception {
                // Arrange
                tournamentResponse.setStatus(TournamentStatus.ACTIVE);
                when(tournamentService.changeStatus(anyString(), any(ChangeStatusRequest.class)))
                                .thenReturn(tournamentResponse);

                // Act & Assert
                mockMvc.perform(patch("/api/tournaments/tournament123/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(changeStatusRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value("tournament123"))
                                .andExpect(jsonPath("$.status").value("ACTIVE"));
        }

        @Test
        @DisplayName("Should return 404 when changing status of non-existent tournament")
        void testChangeStatusNotFound() throws Exception {
                // Arrange
                when(tournamentService.changeStatus(anyString(), any(ChangeStatusRequest.class)))
                                .thenThrow(new ResourceNotFoundException("Tournament not found"));

                // Act & Assert
                mockMvc.perform(patch("/api/tournaments/tournament999/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(changeStatusRequest)))
                                .andExpect(status().isNotFound());
        }
}
