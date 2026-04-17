package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.UserTeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.TeamMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team.Team;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.TeamServiceImpl;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.validator.TeamRequestValidator;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.team.TeamEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.tournament.TournamentEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.entity.user.UserEntity;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.mapper.TeamPersistenceMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TeamRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.UserRepository;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.persistence.repository.TournamentRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    private TeamRequest teamRequest;
    private Team teamModel;
    private TeamEntity teamEntity;
    private TeamResponse expectedResponse;

    @BeforeEach
    void setUp() {
        List<Long> players = new ArrayList<>();
        players.add(1L);
        players.add(2L);

        teamRequest = TeamRequest.builder()
                .name("Test Team")
                .shieldUrl("http://example.com/shield.jpg")
                .uniformColors("Blue and White")
                .tournamentId(10L)
                .captainId(20L)
                .build();

        teamModel = Team.builder()
                .id(1L)
                .name("Test Team")
                .shieldUrl("http://example.com/shield.jpg")
                .uniformColors("Blue and White")
                .tournamentId(10L)
                .captainId(20L)
                .players(new ArrayList<>(players))
                .build();

        teamEntity = TeamEntity.builder()
                .id(1L)
                .name("Test Team")
                .shieldUrl("http://example.com/shield.jpg")
                .uniformColors("Blue and White")
                .tournamentId(10L)
                .captainId(20L)
                .players(new ArrayList<>(players))
                .build();

        expectedResponse = TeamResponse.builder()
                .id(1L)
                .name("Test Team")
                .shieldUrl("http://example.com/shield.jpg")
                .uniformColors("Blue and White")
                .tournamentId(10L)
                .captainId(20L)
                .players(new ArrayList<>(players))
                .build();
    }

    @Test
    @DisplayName("Should create team successfully")
    void shouldCreateTeamSuccessfully() {
        when(teamPersistenceMapper.toEntity(any(Team.class))).thenReturn(teamEntity);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(teamEntity);
        when(teamPersistenceMapper.toModel(any(TeamEntity.class))).thenReturn(teamModel);
        when(teamMapper.toResponse(any(Team.class))).thenReturn(expectedResponse);

        TeamResponse response = teamService.create(teamRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Team", response.getName());
        verify(teamRequestValidator).validate(teamRequest);
        verify(teamRepository).save(any(TeamEntity.class));
    }

    @Test
    @DisplayName("Should get team by id successfully")
    void shouldGetTeamByIdSuccessfully() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(teamEntity));
        when(teamPersistenceMapper.toModel(teamEntity)).thenReturn(teamModel);
        when(teamMapper.toResponse(teamModel)).thenReturn(expectedResponse);

        TeamResponse response = teamService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(teamRepository).findById(1L);
    }

    @Test
    @DisplayName("Should fail when team does not exist")
    void shouldFailWhenTeamDoesNotExist() {
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teamService.getById(999L));
        verify(teamRepository).findById(999L);
    }

    @Test
    @DisplayName("Should remove player successfully")
    void shouldRemovePlayerSuccessfully() {
        TeamEntity updatedEntity = TeamEntity.builder()
                .id(1L)
                .name("Test Team")
                .shieldUrl("http://example.com/shield.jpg")
                .uniformColors("Blue and White")
                .tournamentId(10L)
                .captainId(20L)
                .players(new ArrayList<>(List.of(2L)))
                .build();

        Team updatedModel = Team.builder()
                .id(1L)
                .name("Test Team")
                .shieldUrl("http://example.com/shield.jpg")
                .uniformColors("Blue and White")
                .tournamentId(10L)
                .captainId(20L)
                .players(new ArrayList<>(List.of(2L)))
                .build();

        TeamResponse updatedResponse = TeamResponse.builder()
                .id(1L)
                .name("Test Team")
                .players(new ArrayList<>(List.of(2L)))
                .build();

        when(teamRepository.findById(1L)).thenReturn(Optional.of(teamEntity));
        when(teamPersistenceMapper.toModel(teamEntity)).thenReturn(teamModel);
        when(teamPersistenceMapper.toEntity(any(Team.class))).thenReturn(updatedEntity);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(updatedEntity);
        when(teamPersistenceMapper.toModel(updatedEntity)).thenReturn(updatedModel);
        when(teamMapper.toResponse(updatedModel)).thenReturn(updatedResponse);

        TeamResponse response = teamService.removePlayer(1L, 1L);

        assertNotNull(response);
        assertEquals(List.of(2L), response.getPlayers());
        verify(teamRepository).save(any(TeamEntity.class));
    }

    @Test
    @DisplayName("Should fail when removing unknown player")
    void shouldFailWhenRemovingUnknownPlayer() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(teamEntity));
        when(teamPersistenceMapper.toModel(teamEntity)).thenReturn(teamModel);

        assertThrows(BusinessRuleException.class, () -> teamService.removePlayer(1L, 999L));
        verify(teamRepository, never()).save(any(TeamEntity.class));
    }

    @Test
    @DisplayName("Should delete team successfully")
    void shouldDeleteTeamSuccessfully() {
        when(teamRepository.existsById(1L)).thenReturn(true);

        teamService.delete(1L);

        verify(teamRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should fail deleting non-existent team")
    void shouldFailDeletingNonExistentTeam() {
        when(teamRepository.existsById(404L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> teamService.delete(404L));
        verify(teamRepository, never()).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("Should update team successfully")
    void shouldUpdateTeamSuccessfully() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(teamEntity));
        when(teamPersistenceMapper.toModel(teamEntity)).thenReturn(teamModel);
        when(teamPersistenceMapper.toEntity(any(Team.class))).thenReturn(teamEntity);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(teamEntity);
        when(teamPersistenceMapper.toModel(any(TeamEntity.class))).thenReturn(teamModel);
        when(teamMapper.toResponse(any(Team.class))).thenReturn(expectedResponse);

        TeamResponse response = teamService.update(1L, teamRequest);

        assertNotNull(response);
        assertEquals("Test Team", response.getName());
    }

    @Test
    @DisplayName("Should get team roster successfully")
    void shouldGetRosterSuccessfully() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(teamEntity));
        when(teamPersistenceMapper.toModel(teamEntity)).thenReturn(teamModel);
        when(teamMapper.toResponse(teamModel)).thenReturn(expectedResponse);

        TeamResponse response = teamService.getRoster(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    @DisplayName("Should get teams by tournament successfully")
    void shouldGetTeamsByTournamentSuccessfully() {
        TournamentEntity tournamentEntity = TournamentEntity.builder()
                .id(10L)
                .build();

        when(tournamentRepository.findById(10L))
                .thenReturn(Optional.of(tournamentEntity));
        when(teamRepository.findByTournamentId(10L))
                .thenReturn(List.of(teamEntity));
        when(userRepository.findById(any()))
                .thenReturn(Optional.empty());

        List<?> result = teamService.getTeamsByTournament(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should return user team successfully")
    void shouldGetUserTeamSuccessfully() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(userEntity));
        when(teamRepository.findCurrentTeamByPlayerId(1L))
                .thenReturn(Optional.of(teamEntity));

        UserTeamResponse response = teamService.getUserTeam(1L);

        assertNotNull(response);
        assertTrue(response.isHasTeam());
        assertEquals(teamEntity.getId(), response.getId());
    }

    @Test
    @DisplayName("Should fail when updating non-existent team")
    void shouldFailWhenUpdatingNonExistentTeam() {
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> teamService.update(999L, teamRequest));
        verify(teamRepository, never()).save(any(TeamEntity.class));
    }

    @Test
    @DisplayName("Should fail when getting roster for non-existent team")
    void shouldFailWhenGettingRosterForNonExistentTeam() {
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> teamService.getRoster(999L));
        verify(teamRepository).findById(999L);
    }

    @Test
    @DisplayName("Should fail when getting teams for non-existent tournament")
    void shouldFailWhenGettingTeamsForNonExistentTournament() {
        when(tournamentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> teamService.getTeamsByTournament(999L));
        verify(tournamentRepository).findById(999L);
    }

    @Test
    @DisplayName("Should fail when getting team for non-existent user")
    void shouldFailWhenGettingTeamForNonExistentUser() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> teamService.getUserTeam(999L));
        verify(userRepository).findById(999L);
    }

    @Test
    @DisplayName("Should return user team without team when player has no team")
    void shouldReturnUserTeamWithoutTeamWhenPlayerHasNoTeam() {
        UserEntity userEntity = UserEntity.builder()
                .id(5L)
                .firstName("Solo")
                .lastName("Player")
                .build();

        when(userRepository.findById(5L))
                .thenReturn(Optional.of(userEntity));
        when(teamRepository.findCurrentTeamByPlayerId(5L))
                .thenReturn(Optional.empty());

        UserTeamResponse response = teamService.getUserTeam(5L);

        assertNotNull(response);
        assertFalse(response.isHasTeam());
    }

}
