package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.match;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lineup {

    private Long id;
    private Match match;
    private Team team;
    private String formation;
}
