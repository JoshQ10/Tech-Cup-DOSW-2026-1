package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchPhase;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalle completo de un partido")
public class MatchDetailResponse {

    private Long id;
    private MatchStatus status;
    private Long tournamentId;
    private Long homeTeamId;
    private String homeTeamName;
    private Long awayTeamId;
    private String awayTeamName;
    private Long courtId;
    private String courtName;
    private LocalDate matchDate;
    private LocalTime matchTime;
    private MatchPhase phase;
    private Integer homeScore;
    private Integer awayScore;

    @Schema(description = "Resultado formateado: '2-1' o 'X-X' si fue cancelado")
    private String result;

    private List<MatchGoalEventResponse> goals;
    private List<MatchCardEventResponse> cards;
    private MatchPossessionResponse possession;
}
