package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequest {
    private String name;
    private String shieldUrl;
    private String uniformColors;
    private String tournamentId;
    private String captainId;
}
