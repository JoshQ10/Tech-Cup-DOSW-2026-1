package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.core.model.tournament;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentDate {

    private Long id;
    private Tournament tournament;
    private String description;
    private LocalDate eventDate;
}
