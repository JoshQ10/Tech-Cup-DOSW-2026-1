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
public class TeamResponse {
    private String id;
    private String name;
    private String shieldUrl;
    private String uniformColors;
    private String tournamentId;
    private String captainId;
    private List<String> players;
}
