package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.interface_.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
@DisplayName("TeamController Tests")
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TeamService teamService;

    private TeamRequest teamRequest;
    private TeamResponse teamResponse;

    @BeforeEach
    void setUp() {
        teamRequest = TeamRequest.builder()
                .name("Test Team")
                .shieldUrl("http://example.com/shield.jpg")
                .uniformColors("Blue and White")
                .tournamentId("tournament123")
                .captainId("captain1")
                .build();

        teamResponse = TeamResponse.builder()
                .id("team123")
                .name("Test Team")
                .shieldUrl("http://example.com/shield.jpg")
                .uniformColors("Blue and White")
                .tournamentId("tournament123")
                .captainId("captain1")
                .players(Arrays.asList("player1", "player2"))
                .build();
    }

    @Test
    @DisplayName("Should create team successfully")
    void testCreateTeamSuccess() throws Exception {
        // Arrange
        when(teamService.create(any(TeamRequest.class))).thenReturn(teamResponse);

        // Act & Assert
        mockMvc.perform(post("/api/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("team123"))
                .andExpect(jsonPath("$.name").value("Test Team"));
    }

    @Test
    @DisplayName("Should get team by id successfully")
    void testGetTeamByIdSuccess() throws Exception {
        // Arrange
        when(teamService.getById(anyString())).thenReturn(teamResponse);

        // Act & Assert
        mockMvc.perform(get("/api/teams/team123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("team123"))
                .andExpect(jsonPath("$.name").value("Test Team"));
    }

    @Test
    @DisplayName("Should return 404 when team not found")
    void testGetTeamByIdNotFound() throws Exception {
        // Arrange
        when(teamService.getById(anyString()))
                .thenThrow(new ResourceNotFoundException("Team not found"));

        // Act & Assert
        mockMvc.perform(get("/api/teams/team999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should remove player from team successfully")
    void testRemovePlayerSuccess() throws Exception {
        // Arrange
        teamResponse.setPlayers(Arrays.asList("player2"));
        when(teamService.removePlayer(anyString(), anyString())).thenReturn(teamResponse);

        // Act & Assert
        mockMvc.perform(delete("/api/teams/team123/players/player1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("team123"));
    }

    @Test
    @DisplayName("Should return 404 when removing player from non-existent team")
    void testRemovePlayerTeamNotFound() throws Exception {
        // Arrange
        when(teamService.removePlayer(anyString(), anyString()))
                .thenThrow(new ResourceNotFoundException("Team not found"));

        // Act & Assert
        mockMvc.perform(delete("/api/teams/team999/players/player1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 when removing non-existent player from team")
    void testRemovePlayerNotInTeam() throws Exception {
        // Arrange
        when(teamService.removePlayer(anyString(), anyString()))
                .thenThrow(new BusinessRuleException("Player not found in this team"));

        // Act & Assert
        mockMvc.perform(delete("/api/teams/team123/players/player999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
