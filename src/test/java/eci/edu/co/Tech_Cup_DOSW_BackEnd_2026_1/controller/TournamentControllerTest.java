package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentConfigRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
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
}
