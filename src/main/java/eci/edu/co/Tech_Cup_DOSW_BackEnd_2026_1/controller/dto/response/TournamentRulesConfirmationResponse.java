package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentRulesConfirmationResponse {

    private Long tournamentId;
    private Long userId;
    private LocalDateTime confirmedAt;
    private boolean rulesConfirmed;
}
