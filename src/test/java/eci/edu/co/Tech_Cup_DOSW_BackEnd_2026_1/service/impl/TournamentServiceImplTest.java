package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.service.impl.TournamentServiceImpl;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper.TournamentMapper;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.Tournament;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.TournamentStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TournamentServiceImpl Tests")
class TournamentServiceImplTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TournamentMapper tournamentMapper;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    private TournamentRequest tournamentRequest;
    private ChangeStatusRequest changeStatusRequest;
    private Tournament testTournament;

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

        testTournament = Tournament.builder()
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
    void testCreateTournamentSuccess() {
        // Arrange
        TournamentResponse expectedResponse = TournamentResponse.builder()
                .id(1L).name("Football Tournament 2026-1").teamCount(16)
                .costPerTeam(500000.0).status(TournamentStatus.DRAFT).build();
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(testTournament);
        when(tournamentMapper.toResponse(any(Tournament.class))).thenReturn(expectedResponse);

        // Act
        TournamentResponse response = tournamentService.create(tournamentRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Football Tournament 2026-1", response.getName());
        assertEquals(1L, response.getId());
        assertEquals(16, response.getTeamCount());
        assertEquals(TournamentStatus.DRAFT, response.getStatus());
        verify(tournamentRepository, times(1)).save(any(Tournament.class));
    }

    @Test
    @DisplayName("Should get tournament by id successfully")
    void testGetTournamentByIdSuccess() {
        // Arrange
        TournamentResponse expectedResponse = TournamentResponse.builder()
                .id(1L).name("Football Tournament 2026-1").teamCount(16)
                .costPerTeam(500000.0).status(TournamentStatus.DRAFT).build();
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(testTournament));
        when(tournamentMapper.toResponse(any(Tournament.class))).thenReturn(expectedResponse);

        // Act
        TournamentResponse response = tournamentService.getById(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Football Tournament 2026-1", response.getName());
        assertEquals(1L, response.getId());
        verify(tournamentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should fail when getting non-existent tournament")
    void testGetTournamentByIdNotFound() {
        // Arrange
        when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> tournamentService.getById(1L));
        verify(tournamentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should change tournament status successfully")
    void testChangeStatusSuccess() {
        // Arrange
        TournamentResponse expectedResponse = TournamentResponse.builder()
                .id(1L).name("Football Tournament 2026-1").teamCount(16)
                .costPerTeam(500000.0).status(TournamentStatus.ACTIVE).build();
        testTournament.setStatus(TournamentStatus.ACTIVE);
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(testTournament));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(testTournament);
        when(tournamentMapper.toResponse(any(Tournament.class))).thenReturn(expectedResponse);

        // Act
        TournamentResponse response = tournamentService.changeStatus(1L, changeStatusRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(TournamentStatus.ACTIVE, response.getStatus());
        verify(tournamentRepository, times(1)).findById(1L);
        verify(tournamentRepository, times(1)).save(any(Tournament.class));
    }

    @Test
    @DisplayName("Should fail when changing status of non-existent tournament")
    void testChangeStatusNotFound() {
        // Arrange
        when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> tournamentService.changeStatus(1L, changeStatusRequest));
        verify(tournamentRepository, times(1)).findById(1L);
    }

}
