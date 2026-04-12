package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.TeamMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ValidationException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team.Team;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.TeamServiceImpl;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.TeamRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.TeamPersistenceMapper;
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

    @Mock
    private TeamPersistenceMapper teamPersistenceMapper;

    @InjectMocks
    private TeamServiceImpl teamService;

    private TeamRequest teamRequest;
    private Team testTeam;
    private TeamEntity testTeamEntity;
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

        testTeamEntity = TeamEntity.builder()
                .id(1L)
                .name("Test Team")
                .shieldUrl("http://example.com/shield.jpg")
                .uniformColors("Blue and White")
                .tournamentId(1L)
                .captainId(1L)
                .players(new ArrayList<>(players))
                .build();
    }

    private void stubPersistRoundTrip() {
        when(teamPersistenceMapper.toEntity(any(Team.class))).thenAnswer(inv -> {
                Team t = inv.getArgument(0);
                return TeamEntity.builder()
                                .id(t.getId())
                                .name(t.getName())
                                .shieldUrl(t.getShieldUrl())
                                .uniformColors(t.getUniformColors())
                                .tournamentId(t.getTournamentId())
                                .captainId(t.getCaptainId())
                                .players(t.getPlayers() != null ? new ArrayList<>(t.getPlayers()) : null)
                                .build();
        });
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(testTeamEntity);
        when(teamPersistenceMapper.toModel(any(TeamEntity.class))).thenAnswer(inv -> {
                TeamEntity e = inv.getArgument(0);
                return Team.builder()
                                .id(e.getId())
                                .name(e.getName())
                                .shieldUrl(e.getShieldUrl())
                                .uniformColors(e.getUniformColors())
                                .tournamentId(e.getTournamentId())
                                .captainId(e.getCaptainId())
                                .players(e.getPlayers() != null ? new ArrayList<>(e.getPlayers()) : null)
                                .build();
        });
    }

    @Test
    @DisplayName("Should create team successfully")
    void testCreateTeamSuccess() {
        TeamResponse expectedResponse = TeamResponse.builder()
                .id(1L).name("Test Team").tournamentId(1L).captainId(1L).players(players).build();
        stubPersistRoundTrip();
        when(teamMapper.toResponse(any(Team.class))).thenReturn(expectedResponse);

        TeamResponse response = teamService.create(teamRequest);

        assertNotNull(response);
        assertEquals("Test Team", response.getName());
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getTournamentId());
        verify(teamRepository, times(1)).save(any(TeamEntity.class));
    }

    @Test
    @DisplayName("Should get team by id successfully")
    void testGetTeamByIdSuccess() {
        TeamResponse expectedResponse = TeamResponse.builder()
                .id(1L).name("Test Team").tournamentId(1L).captainId(1L).players(players).build();
        when(teamRepository.findById(1L)).thenReturn(Optional.of(testTeamEntity));
        when(teamPersistenceMapper.toModel(testTeamEntity)).thenReturn(testTeam);
        when(teamMapper.toResponse(any(Team.class))).thenReturn(expectedResponse);

        TeamResponse response = teamService.getById(1L);

        assertNotNull(response);
        assertEquals("Test Team", response.getName());
        assertEquals(1L, response.getId());
        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should fail when getting non-existent team")
    void testGetTeamByIdNotFound() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getById(1L));
        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should remove player from team successfully")
    void testRemovePlayerSuccess() {
        TeamResponse expectedResponse = TeamResponse.builder()
                .id(1L).name("Test Team").tournamentId(1L).captainId(1L).players(players).build();
        when(teamRepository.findById(1L)).thenReturn(Optional.of(testTeamEntity));
        stubPersistRoundTrip();
        when(teamPersistenceMapper.toModel(testTeamEntity)).thenReturn(testTeam);
        when(teamMapper.toResponse(any(Team.class))).thenReturn(expectedResponse);

        TeamResponse response = teamService.removePlayer(1L, 1L);

        assertNotNull(response);
        assertEquals("Test Team", response.getName());
        verify(teamRepository, times(1)).findById(1L);
        verify(teamRepository, times(1)).save(any(TeamEntity.class));
    }

    @Test
    @DisplayName("Should fail when removing player from non-existent team")
    void testRemovePlayerTeamNotFound() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.removePlayer(1L, 1L));
        verify(teamRepository, times(1)).findById(1L);
        verify(teamRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fail when removing non-existent player from team")
    void testRemovePlayerNotInTeam() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(testTeamEntity));
        when(teamPersistenceMapper.toModel(testTeamEntity)).thenReturn(testTeam);

        assertThrows(BusinessRuleException.class, () -> teamService.removePlayer(1L, 999L));
        verify(teamRepository, times(1)).findById(1L);
        verify(teamRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fail when removing player from empty team")
    void testRemovePlayerFromEmptyTeam() {
        testTeam.setPlayers(null);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(testTeamEntity));
        when(teamPersistenceMapper.toModel(testTeamEntity)).thenReturn(testTeam);

        assertThrows(BusinessRuleException.class, () -> teamService.removePlayer(1L, 1L));
        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should validate team request during creation")
    void testCreateTeamValidatesRequest() {
        stubPersistRoundTrip();
        when(teamMapper.toResponse(any(Team.class))).thenReturn(
                TeamResponse.builder().id(1L).name("Test Team").build());

        teamService.create(teamRequest);

        verify(teamRequestValidator, times(1)).validate(teamRequest);
    }

    @Test
    @DisplayName("Should throw ValidationException when request validation fails")
    void testCreateTeamThrowsValidationException() {
        doThrow(new ValidationException("Invalid request", new java.util.HashMap<>()))
                .when(teamRequestValidator).validate(any(TeamRequest.class));

        assertThrows(ValidationException.class, () -> teamService.create(teamRequest));
        verify(teamRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get roster successfully")
    void testGetRosterSuccess() {
        TeamResponse expectedResponse = TeamResponse.builder()
                .id(1L).name("Test Team").players(players).build();
        when(teamRepository.findById(1L)).thenReturn(Optional.of(testTeamEntity));
        when(teamPersistenceMapper.toModel(testTeamEntity)).thenReturn(testTeam);
        when(teamMapper.toResponse(any(Team.class))).thenReturn(expectedResponse);

        TeamResponse response = teamService.getRoster(1L);

        assertNotNull(response);
        assertEquals(players, response.getPlayers());
        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should fail when getting roster of non-existent team")
    void testGetRosterTeamNotFound() {
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getRoster(999L));
        verify(teamRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should preserve captain ID during team creation")
    void testCreateTeamPreservesCaptainId() {
        TeamResponse expectedResponse = TeamResponse.builder()
                .id(1L).name("Test Team").captainId(1L).build();
        stubPersistRoundTrip();
        when(teamMapper.toResponse(any(Team.class))).thenReturn(expectedResponse);

        TeamResponse response = teamService.create(teamRequest);

        assertEquals(1L, response.getCaptainId());
    }

    @Test
    @DisplayName("Should preserve tournament ID during team creation")
    void testCreateTeamPreservesTournamentId() {
        TeamResponse expectedResponse = TeamResponse.builder()
                .id(1L).name("Test Team").tournamentId(1L).build();
        stubPersistRoundTrip();
        when(teamMapper.toResponse(any(Team.class))).thenReturn(expectedResponse);

        TeamResponse response = teamService.create(teamRequest);

        assertEquals(1L, response.getTournamentId());
    }

    @Test
    @DisplayName("Should remove correct player when team has multiple players")
    void testRemoveSpecificPlayerFromMultiple() {
        List<Long> multiplePlay = new ArrayList<>();
        multiplePlay.add(1L);
        multiplePlay.add(2L);
        multiplePlay.add(3L);
        testTeam.setPlayers(multiplePlay);
        testTeamEntity.setPlayers(new ArrayList<>(multiplePlay));

        when(teamRepository.findById(1L)).thenReturn(Optional.of(testTeamEntity));
        stubPersistRoundTrip();
        when(teamPersistenceMapper.toModel(testTeamEntity)).thenReturn(testTeam);
        when(teamMapper.toResponse(any(Team.class))).thenReturn(
                TeamResponse.builder().id(1L).players(multiplePlay).build());

        teamService.removePlayer(1L, 2L);

        assertFalse(testTeam.getPlayers().contains(2L));
        verify(teamRepository).save(any(TeamEntity.class));
    }

    @Test
    @DisplayName("Should handle creation with null shieldUrl")
    void testCreateTeamWithNullShieldUrl() {
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

        TeamEntity entityWithoutShield = TeamEntity.builder()
                .id(2L)
                .name("Test Team")
                .shieldUrl(null)
                .uniformColors("Blue")
                .tournamentId(1L)
                .captainId(1L)
                .players(new ArrayList<>())
                .build();

        when(teamPersistenceMapper.toEntity(any(Team.class))).thenAnswer(inv -> {
                Team t = inv.getArgument(0);
                return TeamEntity.builder()
                                .id(t.getId())
                                .name(t.getName())
                                .shieldUrl(t.getShieldUrl())
                                .uniformColors(t.getUniformColors())
                                .tournamentId(t.getTournamentId())
                                .captainId(t.getCaptainId())
                                .players(t.getPlayers())
                                .build();
        });
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(entityWithoutShield);
        when(teamPersistenceMapper.toModel(entityWithoutShield)).thenReturn(teamWithoutShield);
        when(teamMapper.toResponse(any(Team.class))).thenReturn(
                TeamResponse.builder().id(2L).name("Test Team").shieldUrl(null).build());

        TeamResponse response = teamService.create(requestWithoutShield);

        assertNull(response.getShieldUrl());
        verify(teamRepository).save(any(TeamEntity.class));
    }
}
