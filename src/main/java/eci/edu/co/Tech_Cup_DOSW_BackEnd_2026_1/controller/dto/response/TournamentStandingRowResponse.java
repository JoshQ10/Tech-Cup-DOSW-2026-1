package eci.edu.co.Tech_Cup_DOSW_BackEnd_2026_1.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentStandingRowResponse {

    private int pos;
    private Long teamId;
    private String team;
    private int pj;
    private int pg;
    private int pe;
    private int pp;
    private int gf;
    private int gc;
    private int dc;
    private int pts;
}
