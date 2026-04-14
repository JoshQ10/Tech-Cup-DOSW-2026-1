package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Standing {

    private Long id;
    private Tournament tournament;
    private Team team;
    @Builder.Default
    private int played = 0;
    @Builder.Default
    private int won = 0;
    @Builder.Default
    private int drawn = 0;
    @Builder.Default
    private int lost = 0;
    @Builder.Default
    private int goalsFor = 0;
    @Builder.Default
    private int goalsAgainst = 0;
    @Builder.Default
    private int goalDifference = 0;
    @Builder.Default
    private int points = 0;
}
