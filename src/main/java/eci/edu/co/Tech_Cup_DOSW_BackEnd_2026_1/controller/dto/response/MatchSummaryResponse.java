package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resumen de partido para listados paginados")
public class MatchSummaryResponse {

    private Long id;
    private MatchStatus status;
    private Long homeTeamId;
    private String homeTeamName;
    private Long awayTeamId;
    private String awayTeamName;
    private Long courtId;
    private String courtName;
    private LocalDate matchDate;
    private LocalTime matchTime;
    private Integer homeScore;
    private Integer awayScore;

    @Schema(description = "Resultado formateado: '2-1', 'Por jugar', 'X-X' si fue cancelado")
    private String result;
}
