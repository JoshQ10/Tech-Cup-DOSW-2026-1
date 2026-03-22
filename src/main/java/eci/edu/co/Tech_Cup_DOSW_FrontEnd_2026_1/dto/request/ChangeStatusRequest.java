package eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request;

import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.model.TournamentStatus;
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
