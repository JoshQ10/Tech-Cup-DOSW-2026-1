package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.mapper;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request.TournamentRequest;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response.TournamentResponse;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.TournamentStatus;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.Tournament;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("TournamentMapper Tests")
class TournamentMapperTest {

    @Autowired
    private TournamentMapper tournamentMapper;

    @Test
    @DisplayName("Debe mapear TournamentRequest a entidad Tournament")
    void testToEntity() {
        TournamentRequest request = TournamentRequest.builder()
                .name("Copa DOSW 2026")
                .startDate(LocalDate.of(2026, 4, 1))
                .endDate(LocalDate.of(2026, 6, 30))
                .teamCount(8)
                .costPerTeam(300000.0)
                .build();

        Tournament entity = tournamentMapper.toEntity(request);

        assertNotNull(entity);
        assertEquals("Copa DOSW 2026", entity.getName());
        assertEquals(LocalDate.of(2026, 4, 1), entity.getStartDate());
        assertEquals(LocalDate.of(2026, 6, 30), entity.getEndDate());
        assertEquals(8, entity.getTeamCount());
        assertEquals(300000.0, entity.getCostPerTeam());
        assertNull(entity.getId());
        assertNull(entity.getStatus());
    }

    @Test
    @DisplayName("Debe mapear entidad Tournament a TournamentResponse")
    void testToResponse() {
        Tournament entity = Tournament.builder()
                .id(1L)
                .name("Copa DOSW 2026")
                .startDate(LocalDate.of(2026, 4, 1))
                .endDate(LocalDate.of(2026, 6, 30))
                .teamCount(8)
                .costPerTeam(300000.0)
                .status(TournamentStatus.DRAFT)
                .build();

        TournamentResponse response = tournamentMapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Copa DOSW 2026", response.getName());
        assertEquals(LocalDate.of(2026, 4, 1), response.getStartDate());
        assertEquals(LocalDate.of(2026, 6, 30), response.getEndDate());
        assertEquals(8, response.getTeamCount());
        assertEquals(300000.0, response.getCostPerTeam());
        assertEquals(TournamentStatus.DRAFT, response.getStatus());
    }

    @Test
    @DisplayName("Debe manejar valores nulos en el request")
    void testToEntityWithNulls() {
        TournamentRequest request = TournamentRequest.builder().build();

        Tournament entity = tournamentMapper.toEntity(request);

        assertNotNull(entity);
        assertNull(entity.getName());
        assertNull(entity.getStartDate());
        assertNull(entity.getEndDate());
    }

    @Test
    @DisplayName("Debe mapear todos los estados del torneo correctamente")
    void testToResponseWithAllStatuses() {
        for (TournamentStatus status : TournamentStatus.values()) {
            Tournament entity = Tournament.builder()
                    .id(1L)
                    .name("Test")
                    .status(status)
                    .build();

            TournamentResponse response = tournamentMapper.toResponse(entity);

            assertEquals(status, response.getStatus());
        }
    }
}
