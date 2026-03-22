package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.dto.response;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.model.TournamentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentResponse {
    private String id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int teamCount;
    private double costPerTeam;
    private TournamentStatus status;
}
