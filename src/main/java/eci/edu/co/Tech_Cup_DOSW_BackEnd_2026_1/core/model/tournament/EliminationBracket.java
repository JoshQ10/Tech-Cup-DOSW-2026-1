package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.match.Match;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EliminationBracket {

    private Long id;
    private Tournament tournament;
    private String round;
    private int matchPosition;
    private Match match;
}
