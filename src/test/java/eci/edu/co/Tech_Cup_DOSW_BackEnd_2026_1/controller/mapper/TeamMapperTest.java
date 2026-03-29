package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TeamResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TeamMapper Tests")
class TeamMapperTest {

    private TeamMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TeamMapper.class);
    }

    @Test
    @DisplayName("Should map TeamRequest to Team entity")
    void testMapTeamRequestToEntity() {
        // Arrange
        TeamRequest teamRequest = new TeamRequest();
        teamRequest.setName("Team Alpha");
        teamRequest.setShieldUrl("https://example.com/shield.png");
        teamRequest.setUniformColors("Azul y Blanco");
        teamRequest.setTournamentId(1L);
        teamRequest.setCaptainId(5L);

        // Act
        Team entity = mapper.toEntity(teamRequest);

        // Assert
        assertNotNull(entity);
        assertEquals("Team Alpha", entity.getName());
        assertEquals("https://example.com/shield.png", entity.getShieldUrl());
    }

    @Test
    @DisplayName("Should map null TeamRequest to null")
    void testMapNullTeamRequestToEntity() {
        // Act
        Team entity = mapper.toEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    @DisplayName("Should map Team entity to TeamResponse DTO")
    void testMapEntityToTeamResponse() {
        // Arrange
        Team team = new Team();
        team.setId(1L);
        team.setName("Team Beta");

        // Act
        TeamResponse response = mapper.toResponse(team);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Team Beta", response.getName());
    }

    @Test
    @DisplayName("Should map null Team to null")
    void testMapNullEntityToTeamResponse() {
        // Act
        TeamResponse response = mapper.toResponse(null);

        // Assert
        assertNull(response);
    }

    @Test
    @DisplayName("Should map TeamRequest with special characters")
    void testMapTeamRequestWithSpecialCharacters() {
        // Arrange
        TeamRequest teamRequest = new TeamRequest();
        teamRequest.setName("Equipo Los Tigres");
        teamRequest.setUniformColors("Rojo y Blanco");

        // Act
        Team entity = mapper.toEntity(teamRequest);

        // Assert
        assertNotNull(entity);
        assertEquals("Equipo Los Tigres", entity.getName());
        assertEquals("Rojo y Blanco", entity.getUniformColors());
    }
}
