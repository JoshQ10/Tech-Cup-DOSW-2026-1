package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.TeamServiceImpl;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.Team;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistencia.repository.TeamRepository;
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
    private List<Long> players;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        players.add(1L);
        players.add(2L);

        teamRequest = TeamRequest.builder()
                .name("Test Team")
                .shieldUrl("http://example.com/shield.jpg")
                .uniformColors("Blue and White")
                .tournamentId(1L)
                .captainId(1L)
                .build();

        testTeam = Team.builder()
                .id(1L)
                .name("Test Team")
                .shieldUrl("http://example.com/shield.jpg")
                .uniformColors("Blue and White")
                .tournamentId(1L)
                .captainId(1L)
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
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getTournamentId());
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    @DisplayName("Should get team by id successfully")
    void testGetTeamByIdSuccess() {
        // Arrange
        when(teamRepository.findById(1L)).thenReturn(Optional.of(testTeam));

        // Act
        TeamResponse response = teamService.getById(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Test Team", response.getName());
        assertEquals(1L, response.getId());
        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should fail when getting non-existent team")
    void testGetTeamByIdNotFound() {
        // Arrange
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> teamService.getById(1L));
        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should remove player from team successfully")
    void testRemovePlayerSuccess() {
        // Arrange
        when(teamRepository.findById(1L)).thenReturn(Optional.of(testTeam));
        when(teamRepository.save(any(Team.class))).thenReturn(testTeam);

        // Act
        TeamResponse response = teamService.removePlayer(1L, 1L);

        // Assert
        assertNotNull(response);
        assertEquals("Test Team", response.getName());
        verify(teamRepository, times(1)).findById(1L);
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    @DisplayName("Should fail when removing player from non-existent team")
    void testRemovePlayerTeamNotFound() {
        // Arrange
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> teamService.removePlayer(1L, 1L));
        verify(teamRepository, times(1)).findById(1L);
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    @DisplayName("Should fail when removing non-existent player from team")
    void testRemovePlayerNotInTeam() {
        // Arrange
        when(teamRepository.findById(1L)).thenReturn(Optional.of(testTeam));

        // Act & Assert
        assertThrows(BusinessRuleException.class, () -> teamService.removePlayer(1L, 999L));
        verify(teamRepository, times(1)).findById(1L);
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    @DisplayName("Should fail when removing player from empty team")
    void testRemovePlayerFromEmptyTeam() {
        // Arrange
        testTeam.setPlayers(null);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(testTeam));

        // Act & Assert
        assertThrows(BusinessRuleException.class, () -> teamService.removePlayer(1L, 1L));
        verify(teamRepository, times(1)).findById(1L);
    }
}
