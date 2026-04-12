package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.match;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.enums.MatchEventType;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.team.Team;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchEvent {

    private Long id;
    private Match match;
    private User player;
    private Team team;
    private MatchEventType eventType;
    private int minute;
}
