package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.TeamServiceImpl;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.TeamMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team.Team;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.TeamRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TeamServiceImpl Tests")
@SuppressWarnings("null")
class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMapper teamMapper;

    @Mock
    private TeamRequestValidator teamRequestValidator;

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
        TeamResponse expectedResponse = TeamResponse.builder()
                .id(1L).name("Test Team").tournamentId(1L).captainId(1L).players(players).build();
        when(teamRepository.save(any(Team.class))).thenReturn(testTeam);
        when(teamMapper.toResponse(any(Team.class))).thenReturn(expectedResponse);

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
        TeamResponse expectedResponse = TeamResponse.builder()
                .id(1L).name("Test Team").tournamentId(1L).captainId(1L).players(players).build();
        when(teamRepository.findById(1L)).thenReturn(Optional.of(testTeam));
        when(teamMapper.toResponse(any(Team.class))).thenReturn(expectedResponse);

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
        TeamResponse expectedResponse = TeamResponse.builder()
                .id(1L).name("Test Team").tournamentId(1L).captainId(1L).players(players).build();
        when(teamRepository.findById(1L)).thenReturn(Optional.of(testTeam));
        when(teamRepository.save(any(Team.class))).thenReturn(testTeam);
        when(teamMapper.toResponse(any(Team.class))).thenReturn(expectedResponse);

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

    @Test
    @DisplayName("Should validate team request during creation")
    void testCreateTeamValidatesRequest() {
        // Arrange
        when(teamRepository.save(any(Team.class))).thenReturn(testTeam);
        when(teamMapper.toResponse(any(Team.class))).thenReturn(
                TeamResponse.builder().id(1L).name("Test Team").build());

        // Act
        teamService.create(teamRequest);

        // Assert
        verify(teamRequestValidator, times(1)).validate(teamRequest);
    }

    @Test
    @DisplayName("Should throw ValidationException when request validation fails")
    void testCreateTeamThrowsValidationException() {
        // Arrange
        doThrow(new ValidationException("Invalid request", new java.util.HashMap<>()))
                .when(teamRequestValidator).validate(any(TeamRequest.class));

        // Act & Assert
        assertThrows(ValidationException.class, () -> teamService.create(teamRequest));
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    @DisplayName("Should get roster successfully")
    void testGetRosterSuccess() {
        // Arrange
        TeamResponse expectedResponse = TeamResponse.builder()
                .id(1L).name("Test Team").players(players).build();
        when(teamRepository.findById(1L)).thenReturn(Optional.of(testTeam));
        when(teamMapper.toResponse(any(Team.class))).thenReturn(expectedResponse);

        // Act
        TeamResponse response = teamService.getRoster(1L);

        // Assert
        assertNotNull(response);
        assertEquals(players, response.getPlayers());
        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should fail when getting roster of non-existent team")
    void testGetRosterTeamNotFound() {
        // Arrange
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> teamService.getRoster(999L));
        verify(teamRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should preserve captain ID during team creation")
    void testCreateTeamPreservesCaptainId() {
        // Arrange
        TeamResponse expectedResponse = TeamResponse.builder()
                .id(1L).name("Test Team").captainId(1L).build();
        when(teamRepository.save(any(Team.class))).thenReturn(testTeam);
        when(teamMapper.toResponse(any(Team.class))).thenReturn(expectedResponse);

        // Act
        TeamResponse response = teamService.create(teamRequest);

        // Assert
        assertEquals(1L, response.getCaptainId());
    }

    @Test
    @DisplayName("Should preserve tournament ID during team creation")
    void testCreateTeamPreservesTournamentId() {
        // Arrange
        TeamResponse expectedResponse = TeamResponse.builder()
                .id(1L).name("Test Team").tournamentId(1L).build();
        when(teamRepository.save(any(Team.class))).thenReturn(testTeam);
        when(teamMapper.toResponse(any(Team.class))).thenReturn(expectedResponse);

        // Act
        TeamResponse response = teamService.create(teamRequest);

        // Assert
        assertEquals(1L, response.getTournamentId());
    }

    @Test
    @DisplayName("Should remove correct player when team has multiple players")
    void testRemoveSpecificPlayerFromMultiple() {
        // Arrange
        List<Long> multiplePlay = new ArrayList<>();
        multiplePlay.add(1L);
        multiplePlay.add(2L);
        multiplePlay.add(3L);
        testTeam.setPlayers(multiplePlay);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(testTeam));
        when(teamRepository.save(any(Team.class))).thenReturn(testTeam);
        when(teamMapper.toResponse(any(Team.class))).thenReturn(
                TeamResponse.builder().id(1L).players(multiplePlay).build());

        // Act
        teamService.removePlayer(1L, 2L);

        // Assert
        assertFalse(testTeam.getPlayers().contains(2L));
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    @DisplayName("Should handle creation with null shieldUrl")
    void testCreateTeamWithNullShieldUrl() {
        // Arrange
        TeamRequest requestWithoutShield = TeamRequest.builder()
                .name("Test Team")
                .shieldUrl(null)
                .uniformColors("Blue")
                .tournamentId(1L)
                .captainId(1L)
                .build();

        Team teamWithoutShield = Team.builder()
                .id(2L)
                .name("Test Team")
                .shieldUrl(null)
                .uniformColors("Blue")
                .tournamentId(1L)
                .captainId(1L)
                .players(new ArrayList<>())
                .build();

        when(teamRepository.save(any(Team.class))).thenReturn(teamWithoutShield);
        when(teamMapper.toResponse(any(Team.class))).thenReturn(
                TeamResponse.builder().id(2L).name("Test Team").shieldUrl(null).build());

        // Act
        TeamResponse response = teamService.create(requestWithoutShield);

        // Assert
        assertNull(response.getShieldUrl());
        verify(teamRepository).save(any(Team.class));
    }
}
