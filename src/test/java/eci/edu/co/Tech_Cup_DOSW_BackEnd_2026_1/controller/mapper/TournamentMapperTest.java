package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.TournamentStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TournamentMapper Tests")
class TournamentMapperTest {

    private TournamentMapper tournamentMapper;

    private TournamentRequest tournamentRequest;
    private Tournament tournamentEntity;

    @BeforeEach
    void setUp() {
        tournamentMapper = Mappers.getMapper(TournamentMapper.class);

        tournamentRequest = TournamentRequest.builder()
                .name("Torneo Fútbol 2026-1")
                .startDate(LocalDate.of(2026, 3, 1))
                .endDate(LocalDate.of(2026, 5, 31))
                .teamCount(16)
                .costPerTeam(500000.0)
                .build();

        tournamentEntity = Tournament.builder()
                .id(1L)
                .name("Torneo Fútbol 2026-1")
                .startDate(LocalDate.of(2026, 3, 1))
                .endDate(LocalDate.of(2026, 5, 31))
                .teamCount(16)
                .costPerTeam(500000.0)
                .status(TournamentStatus.DRAFT)
                .createdBy(1L)
                .build();
    }

    @Test
    @DisplayName("Should convert TournamentRequest to Tournament entity successfully")
    void testToEntity() {
        // Act
        Tournament mappedTournament = tournamentMapper.toEntity(tournamentRequest);

        // Assert
        assertThat(mappedTournament)
                .isNotNull()
                .extracting("name", "startDate", "endDate", "teamCount", "costPerTeam")
                .containsExactly("Torneo Fútbol 2026-1",
                        LocalDate.of(2026, 3, 1),
                        LocalDate.of(2026, 5, 31),
                        16,
                        500000.0);
    }

    @Test
    @DisplayName("Should convert Tournament entity to TournamentResponse successfully")
    void testToResponse() {
        // Act
        TournamentResponse response = tournamentMapper.toResponse(tournamentEntity);

        // Assert
        assertThat(response)
                .isNotNull()
                .extracting("id", "name", "startDate", "endDate", "teamCount", "costPerTeam", "status")
                .containsExactly(1L, "Torneo Fútbol 2026-1",
                        LocalDate.of(2026, 3, 1),
                        LocalDate.of(2026, 5, 31),
                        16,
                        500000.0,
                        TournamentStatus.DRAFT);
    }

    @Test
    @DisplayName("Should handle null TournamentRequest in toEntity")
    void testToEntityWithNull() {
        // Act
        Tournament mappedTournament = tournamentMapper.toEntity(null);

        // Assert
        assertThat(mappedTournament).isNull();
    }

    @Test
    @DisplayName("Should handle null Tournament entity in toResponse")
    void testToResponseWithNull() {
        // Act
        TournamentResponse response = tournamentMapper.toResponse(null);

        // Assert
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("Should preserve all fields when mapping TournamentRequest to Tournament")
    void testToEntityPreservesAllFields() {
        // Act
        Tournament mappedTournament = tournamentMapper.toEntity(tournamentRequest);

        // Assert
        assertThat(mappedTournament.getName()).isEqualTo(tournamentRequest.getName());
        assertThat(mappedTournament.getStartDate()).isEqualTo(tournamentRequest.getStartDate());
        assertThat(mappedTournament.getEndDate()).isEqualTo(tournamentRequest.getEndDate());
        assertThat(mappedTournament.getTeamCount()).isEqualTo(tournamentRequest.getTeamCount());
        assertThat(mappedTournament.getCostPerTeam()).isEqualTo(tournamentRequest.getCostPerTeam());
    }

    @Test
    @DisplayName("Should preserve all fields when mapping Tournament to TournamentResponse")
    void testToResponsePreservesAllFields() {
        // Act
        TournamentResponse response = tournamentMapper.toResponse(tournamentEntity);

        // Assert
        assertThat(response.getId()).isEqualTo(tournamentEntity.getId());
        assertThat(response.getName()).isEqualTo(tournamentEntity.getName());
        assertThat(response.getStartDate()).isEqualTo(tournamentEntity.getStartDate());
        assertThat(response.getEndDate()).isEqualTo(tournamentEntity.getEndDate());
        assertThat(response.getTeamCount()).isEqualTo(tournamentEntity.getTeamCount());
        assertThat(response.getCostPerTeam()).isEqualTo(tournamentEntity.getCostPerTeam());
        assertThat(response.getStatus()).isEqualTo(tournamentEntity.getStatus());
    }

    @Test
    @DisplayName("Should handle dates correctly when mapping TournamentRequest")
    void testToEntityWithDifferentDates() {
        // Arrange
        TournamentRequest requestWithDifferentDates = TournamentRequest.builder()
                .name("Another Tournament")
                .startDate(LocalDate.of(2026, 6, 1))
                .endDate(LocalDate.of(2026, 8, 31))
                .teamCount(8)
                .costPerTeam(250000.0)
                .build();

        // Act
        Tournament mappedTournament = tournamentMapper.toEntity(requestWithDifferentDates);

        // Assert
        assertThat(mappedTournament.getStartDate()).isEqualTo(LocalDate.of(2026, 6, 1));
        assertThat(mappedTournament.getEndDate()).isEqualTo(LocalDate.of(2026, 8, 31));
    }

    @Test
    @DisplayName("Should handle different tournament costs correctly")
    void testToEntityWithDifferentCosts() {
        // Arrange
        TournamentRequest requestWithDifferentCost = TournamentRequest.builder()
                .name("Premium Tournament")
                .startDate(LocalDate.of(2026, 9, 1))
                .endDate(LocalDate.of(2026, 11, 30))
                .teamCount(32)
                .costPerTeam(1000000.0)
                .build();

        // Act
        Tournament mappedTournament = tournamentMapper.toEntity(requestWithDifferentCost);

        // Assert
        assertThat(mappedTournament.getCostPerTeam()).isEqualTo(1000000.0);
        assertThat(mappedTournament.getTeamCount()).isEqualTo(32);
    }

    @Test
    @DisplayName("Should handle different tournament statuses in response mapping")
    void testToResponseWithDifferentStatus() {
        // Arrange
        Tournament activeTournament = Tournament.builder()
                .id(2L)
                .name("Active Tournament")
                .startDate(LocalDate.of(2026, 3, 1))
                .endDate(LocalDate.of(2026, 5, 31))
                .teamCount(16)
                .costPerTeam(500000.0)
                .status(TournamentStatus.ACTIVE)
                .createdBy(2L)
                .build();

        // Act
        TournamentResponse response = tournamentMapper.toResponse(activeTournament);

        // Assert
        assertThat(response.getStatus()).isEqualTo(TournamentStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should map tournament with all fields correctly")
    void testToResponseWithAllFields() {
        // Arrange
        Tournament tournamentWithAllFields = Tournament.builder()
                .id(3L)
                .name("Tournament With All Fields")
                .startDate(LocalDate.of(2026, 3, 1))
                .endDate(LocalDate.of(2026, 5, 31))
                .teamCount(16)
                .costPerTeam(500000.0)
                .rules("Regla 1: Todos deben cumplir el reglamento")
                .status(TournamentStatus.DRAFT)
                .createdBy(1L)
                .build();

        // Act
        TournamentResponse response = tournamentMapper.toResponse(tournamentWithAllFields);

        // Assert
        assertThat(response.getId()).isEqualTo(3L);
        assertThat(response.getName()).isEqualTo("Tournament With All Fields");
        assertThat(response.getStatus()).isEqualTo(TournamentStatus.DRAFT);
    }

    @Test
    @DisplayName("Should handle zero cost tournament")
    void testToEntityWithZeroCost() {
        // Arrange
        TournamentRequest freeRequest = TournamentRequest.builder()
                .name("Free Tournament")
                .startDate(LocalDate.of(2026, 12, 1))
                .endDate(LocalDate.of(2026, 12, 31))
                .teamCount(10)
                .costPerTeam(0.0)
                .build();

        // Act
        Tournament mappedTournament = tournamentMapper.toEntity(freeRequest);

        // Assert
        assertThat(mappedTournament.getCostPerTeam()).isEqualTo(0.0);
    }
}
