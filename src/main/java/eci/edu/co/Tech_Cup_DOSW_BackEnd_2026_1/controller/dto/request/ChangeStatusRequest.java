package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.TournamentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStatusRequest {
    private TournamentStatus status;
}
