package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.service.impl;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.ChangeStatusRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.exception.ResourceNotFoundException;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.model.Tournament;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.model.TournamentStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.repository.TournamentRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TournamentServiceImpl Tests")
class TournamentServiceImplTest {

    @Mock
    private TournamentRepository tournamentRepository;

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
    void testCreateTournamentSuccess() {
        // Arrange
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(testTournament);

        // Act
        TournamentResponse response = tournamentService.create(tournamentRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Football Tournament 2026-1", response.getName());
        assertEquals("tournament123", response.getId());
        assertEquals(16, response.getTeamCount());
        assertEquals(TournamentStatus.DRAFT, response.getStatus());
        verify(tournamentRepository, times(1)).save(any(Tournament.class));
    }

    @Test
    @DisplayName("Should get tournament by id successfully")
    void testGetTournamentByIdSuccess() {
        // Arrange
        when(tournamentRepository.findById("tournament123")).thenReturn(Optional.of(testTournament));

        // Act
        TournamentResponse response = tournamentService.getById("tournament123");

        // Assert
        assertNotNull(response);
        assertEquals("Football Tournament 2026-1", response.getName());
        assertEquals("tournament123", response.getId());
        verify(tournamentRepository, times(1)).findById("tournament123");
    }

    @Test
    @DisplayName("Should fail when getting non-existent tournament")
    void testGetTournamentByIdNotFound() {
        // Arrange
        when(tournamentRepository.findById("tournament123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> tournamentService.getById("tournament123"));
        verify(tournamentRepository, times(1)).findById("tournament123");
    }

    @Test
    @DisplayName("Should change tournament status successfully")
    void testChangeStatusSuccess() {
        // Arrange
        testTournament.setStatus(TournamentStatus.ACTIVE);
        when(tournamentRepository.findById("tournament123")).thenReturn(Optional.of(testTournament));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(testTournament);

        // Act
        TournamentResponse response = tournamentService.changeStatus("tournament123", changeStatusRequest);

        // Assert
        assertNotNull(response);
        assertEquals("tournament123", response.getId());
        assertEquals(TournamentStatus.ACTIVE, response.getStatus());
        verify(tournamentRepository, times(1)).findById("tournament123");
        verify(tournamentRepository, times(1)).save(any(Tournament.class));
    }

    @Test
    @DisplayName("Should fail when changing status of non-existent tournament")
    void testChangeStatusNotFound() {
        // Arrange
        when(tournamentRepository.findById("tournament123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> tournamentService.changeStatus("tournament123", changeStatusRequest));
        verify(tournamentRepository, times(1)).findById("tournament123");
    }

}
