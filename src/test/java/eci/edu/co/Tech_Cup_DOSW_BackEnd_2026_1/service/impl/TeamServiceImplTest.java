package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.model.Team;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TeamServiceImpl Tests")
class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    private TeamRequest teamRequest;
    private Team testTeam;
    private List<String> players;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        players.add("player1");
        players.add("player2");

        teamRequest = TeamRequest.builder()
                .name("Test Team")
                .shieldUrl("http://example.com/shield.jpg")
                .uniformColors("Blue and White")
                .tournamentId("tournament123")
                .captainId("captain1")
                .build();

        testTeam = Team.builder()
                .id("team123")
                .name("Test Team")
                .shieldUrl("http://example.com/shield.jpg")
                .uniformColors("Blue and White")
                .tournamentId("tournament123")
                .captainId("captain1")
                .players(players)
                .build();
    }

    @Test
    @DisplayName("Should create team successfully")
    void testCreateTeamSuccess() {
        // Arrange
        when(teamRepository.save(any(Team.class))).thenReturn(testTeam);

        // Act
        TeamResponse response = teamService.create(teamRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Test Team", response.getName());
        assertEquals("team123", response.getId());
        assertEquals("tournament123", response.getTournamentId());
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    @DisplayName("Should get team by id successfully")
    void testGetTeamByIdSuccess() {
        // Arrange
        when(teamRepository.findById("team123")).thenReturn(Optional.of(testTeam));

        // Act
        TeamResponse response = teamService.getById("team123");

        // Assert
        assertNotNull(response);
        assertEquals("Test Team", response.getName());
        assertEquals("team123", response.getId());
        verify(teamRepository, times(1)).findById("team123");
    }

    @Test
    @DisplayName("Should fail when getting non-existent team")
    void testGetTeamByIdNotFound() {
        // Arrange
        when(teamRepository.findById("team123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> teamService.getById("team123"));
        verify(teamRepository, times(1)).findById("team123");
    }

    @Test
    @DisplayName("Should remove player from team successfully")
    void testRemovePlayerSuccess() {
        // Arrange
        when(teamRepository.findById("team123")).thenReturn(Optional.of(testTeam));
        when(teamRepository.save(any(Team.class))).thenReturn(testTeam);

        // Act
        TeamResponse response = teamService.removePlayer("team123", "player1");

        // Assert
        assertNotNull(response);
        assertEquals("Test Team", response.getName());
        verify(teamRepository, times(1)).findById("team123");
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    @DisplayName("Should fail when removing player from non-existent team")
    void testRemovePlayerTeamNotFound() {
        // Arrange
        when(teamRepository.findById("team123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> teamService.removePlayer("team123", "player1"));
        verify(teamRepository, times(1)).findById("team123");
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    @DisplayName("Should fail when removing non-existent player from team")
    void testRemovePlayerNotInTeam() {
        // Arrange
        when(teamRepository.findById("team123")).thenReturn(Optional.of(testTeam));

        // Act & Assert
        assertThrows(BusinessRuleException.class, () -> teamService.removePlayer("team123", "player999"));
        verify(teamRepository, times(1)).findById("team123");
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    @DisplayName("Should fail when removing player from empty team")
    void testRemovePlayerFromEmptyTeam() {
        // Arrange
        testTeam.setPlayers(null);
        when(teamRepository.findById("team123")).thenReturn(Optional.of(testTeam));

        // Act & Assert
        assertThrows(BusinessRuleException.class, () -> teamService.removePlayer("team123", "player1"));
        verify(teamRepository, times(1)).findById("team123");
    }
}
