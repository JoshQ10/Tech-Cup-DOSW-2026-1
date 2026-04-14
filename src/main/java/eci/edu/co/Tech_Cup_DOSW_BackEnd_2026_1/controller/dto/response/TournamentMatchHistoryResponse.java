package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

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
public class TournamentMatchHistoryResponse {

    private Long matchId;
    private LocalDate matchDate;
    private LocalTime matchTime;
    private String phase;
    private Long homeTeamId;
    private String homeTeam;
    private Integer homeScore;
    private Long awayTeamId;
    private String awayTeam;
    private Integer awayScore;
}