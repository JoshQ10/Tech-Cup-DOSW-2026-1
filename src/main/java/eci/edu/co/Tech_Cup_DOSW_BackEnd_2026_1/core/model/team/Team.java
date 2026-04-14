package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    private Long id;
    private String name;
    private String shieldUrl;
    private String uniformColors;
    private Long tournamentId;
    private Long captainId;
    private List<Long> players;
}
