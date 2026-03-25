package eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.service.interface_;

import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.request.TeamRequest;
import eci.edu.co.Tech_Cup_DOSW_FrontEnd_2026_1.dto.response.TeamResponse;

public interface TeamService {
    TeamResponse create(TeamRequest request);
    TeamResponse getById(Long id);
    TeamResponse removePlayer(Long teamId, Long playerId);
}
