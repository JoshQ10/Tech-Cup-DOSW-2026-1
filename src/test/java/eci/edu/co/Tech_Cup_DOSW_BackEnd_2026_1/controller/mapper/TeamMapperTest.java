package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TeamMapper Tests")
class TeamMapperTest {

    private TeamMapper teamMapper;

    private TeamRequest teamRequest;
    private Team teamEntity;

    @BeforeEach
    void setUp() {
        teamMapper = Mappers.getMapper(TeamMapper.class);

        teamRequest = TeamRequest.builder()
                .name("Los Tigres FC")
                .shieldUrl("https://example.com/shield.png")
                .uniformColors("Naranja y Negro")
                .tournamentId(1L)
                .captainId(5L)
                .build();

        teamEntity = Team.builder()
                .id(10L)
                .name("Los Tigres FC")
                .shieldUrl("https://example.com/shield.png")
                .uniformColors("Naranja y Negro")
                .tournamentId(1L)
                .captainId(5L)
                .players(new ArrayList<>(Arrays.asList(1L, 2L, 3L)))
                .build();
    }

    @Test
    @DisplayName("Should convert TeamRequest to Team entity successfully")
    void testToEntity() {
        // Act
        Team mappedTeam = teamMapper.toEntity(teamRequest);

        // Assert
        assertThat(mappedTeam)
                .isNotNull()
                .extracting("name", "shieldUrl", "uniformColors", "tournamentId", "captainId")
                .containsExactly("Los Tigres FC", "https://example.com/shield.png", "Naranja y Negro", 1L, 5L);
    }

    @Test
    @DisplayName("Should convert Team entity to TeamResponse successfully")
    void testToResponse() {
        // Act
        TeamResponse response = teamMapper.toResponse(teamEntity);

        // Assert
        assertThat(response)
                .isNotNull()
                .extracting("id", "name", "shieldUrl", "uniformColors", "tournamentId", "captainId")
                .containsExactly(10L, "Los Tigres FC", "https://example.com/shield.png",
                        "Naranja y Negro", 1L, 5L);
    }

    @Test
    @DisplayName("Should handle null TeamRequest in toEntity")
    void testToEntityWithNull() {
        // Act
        Team mappedTeam = teamMapper.toEntity(null);

        // Assert
        assertThat(mappedTeam).isNull();
    }

    @Test
    @DisplayName("Should handle null Team entity in toResponse")
    void testToResponseWithNull() {
        // Act
        TeamResponse response = teamMapper.toResponse(null);

        // Assert
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("Should preserve all fields when mapping TeamRequest to Team")
    void testToEntityPreservesAllFields() {
        // Act
        Team mappedTeam = teamMapper.toEntity(teamRequest);

        // Assert
        assertThat(mappedTeam.getName()).isEqualTo(teamRequest.getName());
        assertThat(mappedTeam.getShieldUrl()).isEqualTo(teamRequest.getShieldUrl());
        assertThat(mappedTeam.getUniformColors()).isEqualTo(teamRequest.getUniformColors());
        assertThat(mappedTeam.getTournamentId()).isEqualTo(teamRequest.getTournamentId());
        assertThat(mappedTeam.getCaptainId()).isEqualTo(teamRequest.getCaptainId());
    }

    @Test
    @DisplayName("Should preserve all fields when mapping Team to TeamResponse")
    void testToResponsePreservesAllFields() {
        // Act
        TeamResponse response = teamMapper.toResponse(teamEntity);

        // Assert
        assertThat(response.getId()).isEqualTo(teamEntity.getId());
        assertThat(response.getName()).isEqualTo(teamEntity.getName());
        assertThat(response.getShieldUrl()).isEqualTo(teamEntity.getShieldUrl());
        assertThat(response.getUniformColors()).isEqualTo(teamEntity.getUniformColors());
        assertThat(response.getTournamentId()).isEqualTo(teamEntity.getTournamentId());
        assertThat(response.getCaptainId()).isEqualTo(teamEntity.getCaptainId());
        assertThat(response.getPlayers()).isEqualTo(teamEntity.getPlayers());
    }

    @Test
    @DisplayName("Should map Team with player list correctly")
    void testToResponseWithPlayersList() {
        // Act
        TeamResponse response = teamMapper.toResponse(teamEntity);

        // Assert
        assertThat(response.getPlayers())
                .isNotNull()
                .hasSize(3)
                .containsExactly(1L, 2L, 3L);
    }

    @Test
    @DisplayName("Should map Team with empty player list")
    void testToResponseWithEmptyPlayersList() {
        // Arrange
        Team teamWithoutPlayers = Team.builder()
                .id(20L)
                .name("Team Without Players")
                .shieldUrl("https://example.com/shield2.png")
                .uniformColors("Azul y Blanco")
                .tournamentId(2L)
                .captainId(10L)
                .players(new ArrayList<>())
                .build();

        // Act
        TeamResponse response = teamMapper.toResponse(teamWithoutPlayers);

        // Assert
        assertThat(response.getPlayers()).isEmpty();
    }

    @Test
    @DisplayName("Should handle null shieldUrl in TeamRequest")
    void testToEntityWithNullShieldUrl() {
        // Arrange
        TeamRequest requestWithoutShield = TeamRequest.builder()
                .name("Team Without Shield")
                .shieldUrl(null)
                .uniformColors("Rojo y Blanco")
                .tournamentId(3L)
                .captainId(15L)
                .build();

        // Act
        Team mappedTeam = teamMapper.toEntity(requestWithoutShield);

        // Assert
        assertThat(mappedTeam.getShieldUrl()).isNull();
        assertThat(mappedTeam.getName()).isEqualTo("Team Without Shield");
    }

    @Test
    @DisplayName("Should map Team with null players list as null")
    void testToResponseWithNullPlayersList() {
        // Arrange
        Team teamWithNullPlayers = Team.builder()
                .id(30L)
                .name("Team With Null Players")
                .shieldUrl("https://example.com/shield3.png")
                .uniformColors("Verde y Amarillo")
                .tournamentId(4L)
                .captainId(20L)
                .players(null)
                .build();

        // Act
        TeamResponse response = teamMapper.toResponse(teamWithNullPlayers);

        // Assert
        assertThat(response.getPlayers()).isNull();
    }

    @Test
    @DisplayName("Should handle different IDs in TeamRequest")
    void testToEntityWithDifferentIds() {
        // Arrange
        TeamRequest requestWithDifferentIds = TeamRequest.builder()
                .name("Another Team")
                .shieldUrl("https://example.com/shield4.png")
                .uniformColors("Púrpura")
                .tournamentId(999L)
                .captainId(888L)
                .build();

        // Act
        Team mappedTeam = teamMapper.toEntity(requestWithDifferentIds);

        // Assert
        assertThat(mappedTeam.getTournamentId()).isEqualTo(999L);
        assertThat(mappedTeam.getCaptainId()).isEqualTo(888L);
    }
}
