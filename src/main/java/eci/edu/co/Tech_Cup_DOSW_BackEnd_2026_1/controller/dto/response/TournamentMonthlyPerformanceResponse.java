package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentMonthlyPerformanceResponse {

    private Long teamId;
    private String team;
    private List<MonthlyPerformancePoint> monthly;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyPerformancePoint {
        private int month;
        private String monthLabel;
        private int points;
        private int wins;
    }
}