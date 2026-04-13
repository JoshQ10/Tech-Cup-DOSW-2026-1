package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.TeamMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.BusinessRuleException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
}
