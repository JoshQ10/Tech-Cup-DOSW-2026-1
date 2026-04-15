package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

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
public class ActiveTournamentInfoResponse {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private TournamentStatus status;
    private long registeredTeams;
    private long availableCourts;
    private String currentPhase;
}
