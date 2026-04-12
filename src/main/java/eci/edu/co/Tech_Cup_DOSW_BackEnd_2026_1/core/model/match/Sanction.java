package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.match;

import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament.Tournament;
import eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sanction {

    private Long id;
    private Tournament tournament;
    private User player;
    private Match match;
    private String description;
    private LocalDate sanctionDate;
    @Builder.Default
    private int suspendedMatches = 0;
}
