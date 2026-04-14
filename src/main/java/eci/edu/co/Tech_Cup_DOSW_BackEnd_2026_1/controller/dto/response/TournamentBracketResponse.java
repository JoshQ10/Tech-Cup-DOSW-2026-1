package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

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
public class TournamentBracketResponse {

    private Long tournamentId;
    private List<BracketPhaseResponse> phases;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BracketPhaseResponse {
        private String phase;
        private List<String> classifiedTeams;
        private List<BracketMatchResponse> matches;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BracketMatchResponse {
        private Integer slot;
        private Long matchId;
        private LocalDate matchDate;
        private LocalTime matchTime;
        private Long homeTeamId;
        private String homeTeam;
        private Long awayTeamId;
        private String awayTeam;
        private Integer homeScore;
        private Integer awayScore;
        private boolean played;
    }
}
