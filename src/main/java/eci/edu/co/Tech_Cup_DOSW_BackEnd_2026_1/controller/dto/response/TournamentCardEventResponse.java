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
public class TournamentCardEventResponse {

    private String type;
    private LocalDate matchDate;
    private LocalTime matchTime;
    private Long matchId;
    private Long playerId;
    private String player;
    private Integer minute;
}
