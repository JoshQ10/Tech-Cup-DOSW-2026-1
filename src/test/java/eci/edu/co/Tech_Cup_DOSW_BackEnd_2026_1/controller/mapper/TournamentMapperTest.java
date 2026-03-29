package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TournamentMapper Tests")
class TournamentMapperTest {

    private TournamentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TournamentMapper.class);
    }

    @Test
    @DisplayName("Should map TournamentRequest to Tournament entity")
    void testMapTournamentRequestToEntity() {
        // Arrange
        TournamentRequest tournamentRequest = new TournamentRequest();
        tournamentRequest.setName("Football Tournament 2026");
        tournamentRequest.setStartDate(LocalDate.of(2026, 3, 1));
        tournamentRequest.setEndDate(LocalDate.of(2026, 5, 31));
        tournamentRequest.setTeamCount(16);
        tournamentRequest.setCostPerTeam(500000.0);

        // Act
        Tournament entity = mapper.toEntity(tournamentRequest);

        // Assert
        assertNotNull(entity);
        assertEquals("Football Tournament 2026", entity.getName());
        assertEquals(LocalDate.of(2026, 3, 1), entity.getStartDate());
    }

    @Test
    @DisplayName("Should map null TournamentRequest to null")
    void testMapNullTournamentRequestToEntity() {
        // Act
        Tournament entity = mapper.toEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    @DisplayName("Should map Tournament entity to TournamentResponse DTO")
    void testMapEntityToTournamentResponse() {
        // Arrange
        Tournament tournament = new Tournament();
        tournament.setId(1L);
        tournament.setName("International Cup 2026");

        // Act
        TournamentResponse response = mapper.toResponse(tournament);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("International Cup 2026", response.getName());
    }

    @Test
    @DisplayName("Should map null Tournament to null")
    void testMapNullEntityToTournamentResponse() {
        // Act
        TournamentResponse response = mapper.toResponse(null);

        // Assert
        assertNull(response);
    }

    @Test
    @DisplayName("Should map TournamentRequest with different team count")
    void testMapTournamentRequestWithDifferentTeamCount() {
        // Arrange
        TournamentRequest tournamentRequest = new TournamentRequest();
        tournamentRequest.setName("Small Tournament");
        tournamentRequest.setTeamCount(8);
        tournamentRequest.setCostPerTeam(250000.0);

        // Act
        Tournament entity = mapper.toEntity(tournamentRequest);

        // Assert
        assertNotNull(entity);
        assertEquals("Small Tournament", entity.getName());
        assertEquals(8, entity.getTeamCount());
    }
}
