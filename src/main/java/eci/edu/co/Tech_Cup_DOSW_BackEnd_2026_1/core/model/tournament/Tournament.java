package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.TournamentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int teamCount;
    private double costPerTeam;
    private String rules;
    private String sanctionRules;
    private LocalDate inscriptionCloseDate;
    private Long createdBy;
    private TournamentStatus status;
}
