package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.match;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineupPlayer {

    private Long id;
    private Lineup lineup;
    private User player;
    private Integer positionX;
    private Integer positionY;
    @Builder.Default
    private boolean starter = false;
}
